package pe.unsa.vetagenda.service;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.unsa.vetagenda.domain.Domain.*;

@Service
public class CitaService {
  private final Store s;
  private final AgendaService agenda;
  private final InasistenciaService misses;
  private final Clock clock;
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CitaService.class);

  public CitaService(Store s, AgendaService agenda, InasistenciaService misses, Clock clock) {
    this.s = s;
    this.agenda = agenda;
    this.misses = misses;
    this.clock = clock;
  }

  private void role(Actor a) {
    s.require(a.client() || a.reception(), "Tu rol no permite gestionar reservas.");
  }

  private Map<String, Object> pet(Actor a, long id) {
    var p = s.one("select * from pet where id=?", id);
    s.require(
        !a.client() || s.number(p, "owner_id") == a.id(), "Esta mascota no pertenece a tu cuenta.");
    return p;
  }

  private Map<String, Object> appointment(Actor a, long id) {
    var c = s.one("select * from appointment where id=? for update", id);
    pet(a, s.number(c, "pet_id"));
    return c;
  }

  // Lock the veterinarian, then the pet: every reservation follows the same order.
  // This serializes concurrent writes for either resource, including different veterinarians for
  // one pet.
  private void lock(Slot slot) {
    s.one("select id from app_user where id=? for update", slot.vetId());
    s.one("select id from pet where id=? for update", slot.petId());
  }

  @Transactional
  public long reserve(Actor a, Slot slot) {
    role(a);
    var p = pet(a, slot.petId());
    lock(slot);
    agenda.validate(slot);
    if (a.client()) misses.checkOnline(s.number(p, "owner_id"));
    s.require(agenda.free(slot, 0), "Ese horario acaba de ocuparse. Selecciona otro bloque.");
    var now = LocalDateTime.now(clock);
    long id =
        s.insertId(
            "insert into"
                + " appointment(pet_id,vet_id,service_id,starts_at,status,created_at,updated_at)"
                + " values (?,?,?,?,'RESERVADA',?,?)",
            slot.petId(),
            slot.vetId(),
            slot.serviceId(),
            slot.startsAt(),
            now,
            now);
    s.audit(a, "RESERVAR", id, clock);
    log.info("NOTIFICACION_SIMULADA cita={} accion=RESERVAR", id);
    return id;
  }

  private void mutable(Map<String, Object> c) {
    s.require(c.get("status").equals("RESERVADA"), "Solo se puede modificar una cita reservada.");
  }

  private void deadline(Actor a, Map<String, Object> c) {
    s.require(
        !a.client()
            || !((Timestamp) c.get("starts_at"))
                .toLocalDateTime()
                .isBefore(LocalDateTime.now(clock).plusHours(2)),
        "El plazo de 2 horas venció. Comunícate con recepción.");
  }

  @Transactional
  public void cancel(Actor a, long id) {
    role(a);
    var c = appointment(a, id);
    mutable(c);
    deadline(a, c);
    state(a, id, "CANCELADA");
  }

  @Transactional
  public void reschedule(Actor a, long id, Slot slot) {
    role(a);
    var c = appointment(a, id);
    mutable(c);
    deadline(a, c);
    s.require(slot.petId() == s.number(c, "pet_id"), "La mascota de la cita no puede cambiar.");
    s.require(s.number(c, "reschedules") < 2, "La cita alcanzó el límite de 2 reprogramaciones.");
    lock(slot);
    agenda.validate(slot);
    s.require(agenda.free(slot, id), "El nuevo horario no está disponible.");
    s.require(
        !((Timestamp) c.get("starts_at")).toLocalDateTime().equals(slot.startsAt())
            || s.number(c, "vet_id") != slot.vetId(),
        "Selecciona un horario distinto.");
    s.jdbc()
        .update(
            "update appointment set"
                + " vet_id=?,service_id=?,starts_at=?,reschedules=reschedules+1,updated_at=? where"
                + " id=?",
            slot.vetId(),
            slot.serviceId(),
            slot.startsAt(),
            LocalDateTime.now(clock),
            id);
    s.audit(a, "REPROGRAMAR", id, clock);
    log.info("NOTIFICACION_SIMULADA cita={} accion=REPROGRAMAR", id);
  }

  @Transactional
  public void checkin(Actor a, long id) {
    s.require(a.reception(), "Solo recepción puede confirmar la llegada.");
    var c = appointment(a, id);
    mutable(c);
    s.require(
        ((Timestamp) c.get("starts_at"))
            .toLocalDateTime()
            .toLocalDate()
            .equals(LocalDate.now(clock)),
        "Confirma la llegada el día de la cita.");
    state(a, id, "EN_ESPERA");
  }

  @Transactional
  public void noShow(Actor a, long id) {
    s.require(a.reception(), "Solo recepción puede registrar inasistencias.");
    var c = appointment(a, id);
    mutable(c);
    s.require(
        !LocalDateTime.now(clock)
            .isBefore(((Timestamp) c.get("starts_at")).toLocalDateTime().plusMinutes(15)),
        "Espera al menos 15 minutos después de la hora de la cita.");
    state(a, id, "NO_ASISTIO");
  }

  @Transactional
  public void attend(Actor a, long id, Visit visit) {
    s.require(a.vet(), "Solo el veterinario asignado puede registrar la atención.");
    var c = appointment(a, id);
    s.require(a.id() == s.number(c, "vet_id"), "La cita está asignada a otro veterinario.");
    s.require(c.get("status").equals("EN_ESPERA"), "La cita debe estar en espera.");
    text(visit.reason(), 1000, "motivo");
    text(visit.diagnosis(), 2000, "diagnóstico");
    text(visit.treatment(), 2000, "tratamiento");
    s.require(
        visit.weight() != null
            && visit.weight().signum() > 0
            && visit.weight().compareTo(new java.math.BigDecimal("500")) <= 0,
        "El peso debe ser mayor que 0 y menor o igual a 500 kg.");
    s.jdbc()
        .update(
            "insert into"
                + " clinical_record(appointment_id,reason,diagnosis,treatment,weight,created_at)"
                + " values (?,?,?,?,?,?)",
            id,
            visit.reason().strip(),
            visit.diagnosis().strip(),
            visit.treatment().strip(),
            visit.weight(),
            LocalDateTime.now(clock));
    state(a, id, "ATENDIDA");
  }

  private void text(String v, int max, String field) {
    s.require(
        v != null && !v.isBlank() && v.length() <= max,
        "Completa el " + field + " dentro del límite permitido.");
  }

  private void state(Actor a, long id, String status) {
    s.jdbc()
        .update(
            "update appointment set status=?,updated_at=? where id=?",
            status,
            LocalDateTime.now(clock),
            id);
    s.audit(a, status, id, clock);
    log.info("NOTIFICACION_SIMULADA cita={} estado={}", id, status);
  }

  public List<Map<String, Object>> history(Actor a, long petId) {
    pet(a, petId);
    if (a.vet())
      s.require(
          s.jdbc()
                  .queryForObject(
                      "select count(*) from appointment where pet_id=? and vet_id=?",
                      Integer.class,
                      petId,
                      a.id())
              > 0,
          "Solo puedes consultar mascotas de tus citas.");
    return s.jdbc()
        .queryForList(
            "select r.*,a.starts_at,v.name vet_name from clinical_record r join appointment a on"
                + " a.id=r.appointment_id join app_user v on v.id=a.vet_id where a.pet_id=? order"
                + " by a.starts_at desc",
            petId);
  }
}
