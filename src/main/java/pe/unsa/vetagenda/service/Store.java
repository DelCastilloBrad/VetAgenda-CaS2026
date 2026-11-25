package pe.unsa.vetagenda.service;

import java.time.*;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.unsa.vetagenda.domain.Domain.*;

@Repository
public class Store {
  private final JdbcTemplate db;

  public JdbcTemplate jdbc() {
    return db;
  }

  public Store(JdbcTemplate db) {
    this.db = db;
  }

  public Map<String, Object> one(String sql, Object... args) {
    var rows = db.queryForList(sql, args);
    if (rows.isEmpty()) throw new RuleViolation("El registro solicitado no existe.");
    return rows.getFirst();
  }

  public long number(Map<String, Object> row, String key) {
    return ((Number) row.get(key)).longValue();
  }

  public Actor actor(String email) {
    var u = one("select * from app_user where email=?", email);
    return new Actor(number(u, "id"), (String) u.get("name"), email, (String) u.get("role"));
  }

  public void audit(Actor a, String action, long entity, Clock clock) {
    db.update(
        "insert into audit_event(actor_id,action,entity_id,created_at) values (?,?,?,?)",
        a.id(),
        action,
        entity,
        LocalDateTime.now(clock));
  }

  public long insertId(String sql, Object... args) {
    var holder = new org.springframework.jdbc.support.GeneratedKeyHolder();
    db.update(
        c -> {
          var s = c.prepareStatement(sql, new String[] {"id"});
          for (int i = 0; i < args.length; i++) s.setObject(i + 1, args[i]);
          return s;
        },
        holder);
    return Objects.requireNonNull(holder.getKey()).longValue();
  }

  public List<Map<String, Object>> pets(Actor a) {
    return db.queryForList(
        "select p.*,u.name owner_name from pet p join app_user u on u.id=p.owner_id "
            + (a.client() ? "where p.owner_id=?" : "")
            + " order by p.name",
        a.client() ? new Object[] {a.id()} : new Object[] {});
  }

  public List<Map<String, Object>> appointments(Actor a) {
    String filter = a.client() ? "where p.owner_id=?" : a.vet() ? "where a.vet_id=?" : "";
    return db.queryForList(
        "select a.*,p.name pet_name,u.name owner_name,v.name vet_name,s.name service_name from"
            + " appointment a join pet p on p.id=a.pet_id join app_user u on u.id=p.owner_id join"
            + " app_user v on v.id=a.vet_id join clinical_service s on s.id=a.service_id "
            + filter
            + " order by a.starts_at desc",
        filter.isEmpty() ? new Object[] {} : new Object[] {a.id()});
  }

  public List<Map<String, Object>> vets() {
    return db.queryForList(
        "select id,name from app_user where role='VETERINARIO' and active=true order by name");
  }

  public List<Map<String, Object>> services() {
    return db.queryForList("select * from clinical_service where active=true order by name");
  }

  public List<Map<String, Object>> clients() {
    return db.queryForList(
        "select id,name,email from app_user where role='CLIENTE' and active=true order by name");
  }

  public void require(boolean condition, String message) {
    if (!condition) throw new RuleViolation(message);
  }
}
