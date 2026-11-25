package pe.unsa.vetagenda.config;

import java.time.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.stereotype.Component;
import pe.unsa.vetagenda.service.*;

@Component
public class DemoData implements ApplicationRunner {
  private final Store s;
  private final UsuarioService users;
  private final boolean enabled;
  private final Clock clock;

  public DemoData(
      Store s,
      UsuarioService users,
      @Value("${vetagenda.demo.enabled:false}") boolean enabled,
      Clock clock) {
    this.s = s;
    this.users = users;
    this.enabled = enabled;
    this.clock = clock;
  }

  public void run(ApplicationArguments args) {
    if (!enabled || s.jdbc().queryForObject("select count(*) from app_user", Integer.class) > 0)
      return;
    String[] roles = {"ADMIN", "RECEPCION", "VETERINARIO", "CLIENTE"};
    String[] names = {
      "Administración Demo", "Lucía · Recepción", "Dra. Camila Torres", "Daniela Flores"
    };
    String[] emails = {
      "admin@vetagenda.test",
      "recepcion@vetagenda.test",
      "vet@vetagenda.test",
      "cliente@vetagenda.test"
    };
    long[] ids = new long[4];
    for (int i = 0; i < 4; i++)
      ids[i] =
          users.register(
              names[i], emails[i], "DemoVet2026!", "1000000" + i, "90000000" + i, true, roles[i]);
    var client = s.actor(emails[3]);
    long pet =
        users.savePet(
            client, null, ids[3], "Luna", "Perro", "Mestiza", LocalDate.now(clock).minusYears(3));
    users.savePet(
        client, null, ids[3], "Milo", "Gato", "Europeo", LocalDate.now(clock).minusYears(1));
    var now = LocalDateTime.now(clock);
    var at = LocalDate.now(clock).atTime(10, 0);
    s.jdbc()
        .update(
            "insert into"
                + " appointment(pet_id,vet_id,service_id,starts_at,status,created_at,updated_at)"
                + " values (?,?,1,?,'RESERVADA',?,?)",
            pet,
            ids[2],
            at,
            now,
            now);
  }
}
