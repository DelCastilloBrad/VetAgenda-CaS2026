package pe.unsa.vetagenda.service;

import java.sql.Time;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import pe.unsa.vetagenda.domain.Domain.*;

@Service
public class AgendaService {
  private final Store s;
  private final Clock clock;

  public AgendaService(Store s, Clock clock) {
    this.s = s;
    this.clock = clock;
  }

  public void validate(Slot slot) {
    s.require(slot.startsAt() != null, "Selecciona una fecha y hora.");
    var at = slot.startsAt();
    var now = LocalDateTime.now(clock);
    s.require(!at.isBefore(now.plusHours(2)), "Reserva con al menos 2 horas de anticipación.");
    s.require(
        !at.isAfter(now.plusDays(30)), "Solo se admiten reservas hasta 30 días de anticipación.");
    s.require(
        at.getSecond() == 0 && at.getNano() == 0 && at.getMinute() % 30 == 0,
        "Selecciona un bloque de 30 minutos.");
    var hours =
        s.one("select * from business_hours where day_of_week=?", at.getDayOfWeek().getValue());
    s.require((Boolean) hours.get("active"), "La clínica no atiende ese día.");
    var opening = ((Time) hours.get("opening")).toLocalTime();
    var closing = ((Time) hours.get("closing")).toLocalTime();
    s.require(
        !at.toLocalTime().isBefore(opening)
            && !at.toLocalTime().plusMinutes(30).isAfter(closing)
            && at.toLocalTime().isBefore(closing),
        "El bloque está fuera del horario de atención.");
    s.require(
        s.jdbc()
                .queryForObject(
                    "select count(*) from holiday where holiday_date=?",
                    Integer.class,
                    at.toLocalDate())
            == 0,
        "La clínica no atiende en el feriado seleccionado.");
    var vet = s.one("select role,active from app_user where id=?", slot.vetId());
    s.require(
        vet.get("role").equals("VETERINARIO") && (Boolean) vet.get("active"),
        "Selecciona un veterinario activo.");
    s.require(
        s.jdbc()
                .queryForObject(
                    "select count(*) from clinical_service where id=? and active=true",
                    Integer.class,
                    slot.serviceId())
            == 1,
        "Selecciona un servicio activo.");
  }

  public boolean free(Slot slot, long exclude) {
    return s.jdbc()
            .queryForObject(
                "select count(*) from appointment where id<>? and status in"
                    + " ('RESERVADA','EN_ESPERA') and starts_at=? and (vet_id=? or pet_id=?)",
                Integer.class,
                exclude,
                slot.startsAt(),
                slot.vetId(),
                slot.petId())
        == 0;
  }

  public List<LocalDateTime> available(LocalDate date, long pet, long vet, long service) {
    List<LocalDateTime> slots = new ArrayList<>();
    for (int minute = 0; minute < 24 * 60; minute += 30) {
      var at = date.atStartOfDay().plusMinutes(minute);
      var slot = new Slot(pet, vet, service, at);
      try {
        validate(slot);
        if (free(slot, 0)) slots.add(at);
      } catch (RuleViolation ignored) {
        /* An unavailable block is excluded. */
      }
    }
    return slots;
  }
}
