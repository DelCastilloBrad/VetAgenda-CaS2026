package pe.unsa.vetagenda;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pe.unsa.vetagenda.domain.Domain.*;
import pe.unsa.vetagenda.service.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BusinessTests {
  @TestConfiguration
  static class Time {
    @Bean
    @Primary
    Clock fixedClock() {
      return Clock.fixed(Instant.parse("2026-11-24T15:00:00Z"), ZoneId.of("America/Lima"));
    }
  }

  @Autowired Store s;
  @Autowired CitaService citas;
  @Autowired AgendaService agenda;
  @Autowired InasistenciaService misses;
  @Autowired UsuarioService users;
  @Autowired ThemeService themes;
  @Autowired Clock clock;
  Actor client, reception, vet, admin;
  long pet, service;
  LocalDateTime next = LocalDateTime.of(2026, 11, 25, 10, 0);

  @BeforeEach
  void setup() {
    client = s.actor("cliente@vetagenda.test");
    reception = s.actor("recepcion@vetagenda.test");
    vet = s.actor("vet@vetagenda.test");
    admin = s.actor("admin@vetagenda.test");
    pet = s.number(s.pets(client).getFirst(), "id");
    service = s.number(s.services().getFirst(), "id");
  }

  Slot slot() {
    return new Slot(pet, vet.id(), service, next);
  }

  void rejects(Runnable action, String message) {
    assertThatThrownBy(action::run).isInstanceOf(RuleViolation.class).hasMessageContaining(message);
  }

  long createToday(String status, int minutes) {
    var at = LocalDateTime.now(clock).plusMinutes(minutes);
    return s.insertId(
        "insert into appointment(pet_id,vet_id,service_id,starts_at,status,created_at,updated_at)"
            + " values (?,?,?,?,?,?,?)",
        pet,
        vet.id(),
        service,
        at,
        status,
        at,
        at);
  }

  @Test
  void reservesValidSlot() {
    long id = citas.reserve(client, slot());
    assertThat(s.one("select status from appointment where id=?", id).get("status"))
        .isEqualTo("RESERVADA");
  }

  @Test
  void rejectsSameVeterinarianSlot() {
    citas.reserve(client, slot());
    rejects(() -> citas.reserve(reception, slot()), "ocuparse");
  }

  @Test
  void rejectsSamePetWithDifferentVeterinarian() {
    long other =
        users.register(
            "Otro Vet",
            "otrovet@test.pe",
            "OtraClave2026!",
            "12345678",
            "999999999",
            true,
            "VETERINARIO");
    citas.reserve(client, slot());
    rejects(() -> citas.reserve(client, new Slot(pet, other, service, next)), "ocuparse");
  }

  @Test
  void rejectsUnderTwoHours() {
    rejects(
        () ->
            citas.reserve(
                client, new Slot(pet, vet.id(), service, LocalDateTime.now(clock).plusMinutes(90))),
        "2 horas");
  }

  @Test
  void acceptsExactTwoHourBoundary() {
    assertThat(
            citas.reserve(
                client, new Slot(pet, vet.id(), service, LocalDateTime.now(clock).plusHours(2))))
        .isPositive();
  }

  @Test
  void acceptsThirtyDayBoundary() {
    assertThat(
            citas.reserve(
                client, new Slot(pet, vet.id(), service, LocalDateTime.now(clock).plusDays(30))))
        .isPositive();
  }

  @Test
  void rejectsOverThirtyDays() {
    rejects(
        () -> citas.reserve(client, new Slot(pet, vet.id(), service, next.plusDays(30))),
        "30 días");
  }

  @Test
  void rejectsSunday() {
    rejects(
        () ->
            citas.reserve(
                client, new Slot(pet, vet.id(), service, LocalDateTime.of(2026, 11, 29, 10, 0))),
        "no atiende");
  }

  @Test
  void rejectsHoliday() {
    s.jdbc().update("insert into holiday values (?,?)", next.toLocalDate(), "Cierre de prueba");
    rejects(() -> citas.reserve(client, slot()), "feriado");
  }

  @Test
  void rejectsOffGrid() {
    rejects(
        () -> citas.reserve(client, new Slot(pet, vet.id(), service, next.plusMinutes(5))),
        "30 minutos");
  }

  @Test
  void acceptsLastBlock() {
    assertThat(
            citas.reserve(
                client, new Slot(pet, vet.id(), service, next.withHour(18).withMinute(30))))
        .isPositive();
  }

  @Test
  void rejectsClosingTime() {
    rejects(
        () -> citas.reserve(client, new Slot(pet, vet.id(), service, next.withHour(19))),
        "fuera del horario");
  }

  @Test
  void rejectsOtherOwnersPet() {
    long id =
        users.register(
            "Otro Cliente",
            "otro@test.pe",
            "OtraClave2026!",
            "12345679",
            "999999998",
            true,
            "CLIENTE");
    Actor other = s.actor("otro@test.pe");
    rejects(() -> citas.reserve(other, slot()), "no pertenece");
  }

  @Test
  void cancelsAndReleasesSlot() {
    long id = citas.reserve(client, slot());
    citas.cancel(client, id);
    assertThat(citas.reserve(client, slot())).isPositive();
  }

  @Test
  void rejectsLateClientCancellation() {
    long id = createToday("RESERVADA", 30);
    rejects(() -> citas.cancel(client, id), "plazo");
    citas.cancel(reception, id);
    assertThat(s.one("select status from appointment where id=?", id).get("status"))
        .isEqualTo("CANCELADA");
  }

  @Test
  void enforcesTwoReschedules() {
    long id = citas.reserve(client, slot());
    citas.reschedule(client, id, new Slot(pet, vet.id(), service, next.plusMinutes(30)));
    citas.reschedule(client, id, new Slot(pet, vet.id(), service, next.plusHours(1)));
    rejects(
        () -> citas.reschedule(client, id, new Slot(pet, vet.id(), service, next.plusHours(2))),
        "límite de 2");
  }

  @Test
  void checkinOnlyByReception() {
    long id = createToday("RESERVADA", 0);
    rejects(() -> citas.checkin(client, id), "recepción");
    citas.checkin(reception, id);
    assertThat(s.one("select status from appointment where id=?", id).get("status"))
        .isEqualTo("EN_ESPERA");
  }

  @Test
  void checkinOnlyOnAppointmentDay() {
    long id = citas.reserve(client, slot());
    rejects(() -> citas.checkin(reception, id), "día de la cita");
  }

  @Test
  void noShowAtFifteenMinuteBoundary() {
    long id = createToday("RESERVADA", -15);
    citas.noShow(reception, id);
    assertThat(misses.count(client.id())).isEqualTo(1);
  }

  @Test
  void rejectsEarlyNoShow() {
    long id = createToday("RESERVADA", -14);
    rejects(() -> citas.noShow(reception, id), "15 minutos");
  }

  @Test
  void blocksThreeMissesButReceptionMayReserve() {
    for (int i = 1; i <= 3; i++) createToday("NO_ASISTIO", -1440 * i);
    rejects(() -> citas.reserve(client, slot()), "3 inasistencias");
    assertThat(citas.reserve(reception, slot())).isPositive();
  }

  @Test
  void ignoresMissesOlderThanNinetyDays() {
    for (int i = 91; i <= 93; i++) createToday("NO_ASISTIO", -1440 * i);
    assertThat(misses.count(client.id())).isZero();
    assertThat(citas.reserve(client, slot())).isPositive();
  }

  @Test
  void attendsAndPreservesHistory() {
    long id = createToday("EN_ESPERA", 0);
    citas.attend(
        vet, id, new Visit("Control", "Salud estable", "Continuar control", new BigDecimal("4.5")));
    assertThat(citas.history(client, pet)).hasSize(1);
    assertThat(s.one("select status from appointment where id=?", id).get("status"))
        .isEqualTo("ATENDIDA");
    rejects(() -> citas.cancel(reception, id), "reservada");
  }

  @Test
  void rejectsAttentionBeforeCheckin() {
    long id = createToday("RESERVADA", 0);
    rejects(
        () -> citas.attend(vet, id, new Visit("Control", "Normal", "Control", BigDecimal.ONE)),
        "en espera");
  }

  @Test
  void rejectsUnassignedVeterinarian() {
    long id = createToday("EN_ESPERA", 0);
    var another = new Actor(999, "Otro", "otro@test.pe", "VETERINARIO");
    rejects(
        () -> citas.attend(another, id, new Visit("Control", "Normal", "Control", BigDecimal.ONE)),
        "otro veterinario");
  }

  @Test
  void rejectsInvalidClinicalWeight() {
    long id = createToday("EN_ESPERA", 0);
    rejects(
        () -> citas.attend(vet, id, new Visit("Control", "Normal", "Control", BigDecimal.ZERO)),
        "peso");
    assertThat(
            s.jdbc()
                .queryForObject(
                    "select count(*) from clinical_record where appointment_id=?",
                    Integer.class,
                    id))
        .isZero();
  }

  @Test
  void validatesConsentDniAndPassword() {
    rejects(
        () ->
            users.register(
                "Nombre", "nuevo@test.pe", "ValidClave2026!", "1234", "999999999", true, "CLIENTE"),
        "DNI");
    rejects(
        () ->
            users.register(
                "Nombre",
                "nuevo@test.pe",
                "ValidClave2026!",
                "12345678",
                "999999999",
                false,
                "CLIENTE"),
        "aceptar");
    rejects(
        () ->
            users.register(
                "Nombre", "nuevo@test.pe", "abc", "12345678", "999999999", true, "CLIENTE"),
        "contraseña");
  }

  @Test
  void duplicateAndInvalidEmailsAreRejected() {
    rejects(
        () ->
            users.register(
                "Nombre",
                "sin-arroba",
                "ValidClave2026!",
                "12345678",
                "999999999",
                true,
                "CLIENTE"),
        "correo");
    rejects(
        () ->
            users.register(
                "Nombre",
                client.email(),
                "ValidClave2026!",
                "12345678",
                "999999999",
                true,
                "CLIENTE"),
        "registrado");
  }

  @Test
  void bcryptAndOwnedPetValidation() {
    long id =
        users.register(
            "Prueba", "nuevo@test.pe", "ValidClave2026!", "12345678", "999999999", true, "CLIENTE");
    assertThat((String) s.one("select password from app_user where id=?", id).get("password"))
        .startsWith("$2");
    rejects(
        () ->
            users.savePet(
                client, null, client.id(), "Nueva", "Perro", "", LocalDate.now(clock).plusDays(1)),
        "futuro");
  }

  @Test
  void themeValidatesValuesAndRoles() {
    rejects(() -> themes.save(client, Map.of()), "administración");
    rejects(() -> themes.save(admin, Map.of("primary", "red;display:none")), "hexadecimales");
    themes.save(admin, Map.of("primary", "#102030", "scale", "120"));
    assertThat(themes.css()).contains("--va-primary:#102030").contains("--va-font-scale:120%");
    themes.save(admin, ThemeService.DEFAULTS);
    assertThat(themes.current()).isEqualTo(ThemeService.DEFAULTS);
  }

  @Test
  void adminCannotDisableSelf() {
    rejects(() -> users.setActive(admin, admin.id(), false), "propia cuenta");
  }
}
