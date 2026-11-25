package pe.unsa.vetagenda.web;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.unsa.vetagenda.domain.Domain.*;
import pe.unsa.vetagenda.service.*;

@Controller
public class WebController {
  private final Store s;
  private final UsuarioService users;
  private final CitaService citas;
  private final AgendaService agenda;
  private final InasistenciaService misses;
  private final ThemeService themes;
  private final Clock clock;

  public WebController(
      Store s,
      UsuarioService users,
      CitaService citas,
      AgendaService agenda,
      InasistenciaService misses,
      ThemeService themes,
      Clock clock) {
    this.s = s;
    this.users = users;
    this.citas = citas;
    this.agenda = agenda;
    this.misses = misses;
    this.themes = themes;
    this.clock = clock;
  }

  @ModelAttribute
  public void common(Principal p, Model m) {
    if (p != null) m.addAttribute("actor", s.actor(p.getName()));
    m.addAttribute("today", LocalDate.now(clock));
  }

  private Actor actor(Principal p) {
    return s.actor(p.getName());
  }

  private String ok(RedirectAttributes r, String message, String path) {
    r.addFlashAttribute("success", message);
    return "redirect:" + path;
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/registro")
  public String register() {
    return "register";
  }

  @PostMapping("/registro")
  public String register(
      @RequestParam String name,
      @RequestParam String email,
      @RequestParam String password,
      @RequestParam String dni,
      @RequestParam String phone,
      @RequestParam(defaultValue = "false") boolean consent,
      RedirectAttributes r) {
    users.register(name, email, password, dni, phone, consent, "CLIENTE");
    return ok(r, "Cuenta creada. Ya puedes iniciar sesión.", "/login");
  }

  @GetMapping("/")
  public String dashboard(Principal p, Model m) {
    var a = actor(p);
    var rows = s.appointments(a);
    var today = LocalDate.now(clock);
    m.addAttribute(
        "appointments",
        rows.stream()
            .filter(
                x ->
                    ((java.sql.Timestamp) x.get("starts_at"))
                        .toLocalDateTime()
                        .toLocalDate()
                        .equals(today))
            .sorted(Comparator.comparing(x -> ((java.sql.Timestamp) x.get("starts_at"))))
            .toList());
    m.addAttribute("total", rows.size());
    m.addAttribute(
        "waiting", rows.stream().filter(x -> x.get("status").equals("EN_ESPERA")).count());
    m.addAttribute(
        "attended", rows.stream().filter(x -> x.get("status").equals("ATENDIDA")).count());
    m.addAttribute("misses", a.client() ? misses.count(a.id()) : 0);
    return "dashboard";
  }

  @GetMapping("/citas")
  public String appointments(Principal p, Model m, @RequestParam(required = false) String state) {
    var rows = s.appointments(actor(p));
    if (state != null && !state.isBlank())
      rows = rows.stream().filter(x -> x.get("status").equals(state)).toList();
    m.addAttribute("appointments", rows);
    return "appointments";
  }

  @GetMapping("/mascotas")
  public String pets(Principal p, Model m, @RequestParam(required = false) Long edit) {
    var a = actor(p);
    m.addAttribute("pets", s.pets(a));
    m.addAttribute("clients", s.clients());
    if (edit != null) {
      var pet = s.one("select * from pet where id=?", edit);
      s.require(
          !a.client() || s.number(pet, "owner_id") == a.id(),
          "Esta mascota no pertenece a tu cuenta.");
      m.addAttribute("editing", pet);
    }
    return "pets";
  }

  @PostMapping("/mascotas")
  public String pet(
      Principal p,
      @RequestParam(required = false) Long id,
      @RequestParam(defaultValue = "0") long ownerId,
      @RequestParam String name,
      @RequestParam String species,
      @RequestParam(defaultValue = "") String breed,
      @RequestParam(required = false) LocalDate birth,
      RedirectAttributes r) {
    users.savePet(actor(p), id, ownerId, name, species, breed, birth);
    return ok(r, "Mascota guardada.", "/mascotas");
  }

  @GetMapping({"/reservar", "/disponibilidad"})
  public String booking(
      Principal p,
      Model m,
      @RequestParam(required = false) Long petId,
      @RequestParam(required = false) Long vetId,
      @RequestParam(required = false) Long serviceId,
      @RequestParam(required = false) LocalDate date,
      @RequestParam(required = false) Long appointmentId) {
    var a = actor(p);
    var pets = s.pets(a);
    if (appointmentId != null) {
      var c =
          s.one(
              "select a.*,p.owner_id from appointment a join pet p on p.id=a.pet_id where a.id=?",
              appointmentId);
      s.require(
          !a.client() || s.number(c, "owner_id") == a.id(), "La cita no pertenece a tu cuenta.");
      s.require(c.get("status").equals("RESERVADA"), "Solo puedes reprogramar citas reservadas.");
      petId = s.number(c, "pet_id");
      m.addAttribute("rescheduling", c);
    }
    m.addAttribute("pets", pets);
    m.addAttribute("vets", s.vets());
    m.addAttribute("services", s.services());
    m.addAttribute("date", date == null ? LocalDate.now(clock).plusDays(1) : date);
    m.addAttribute("selectedPet", petId);
    m.addAttribute("selectedVet", vetId);
    m.addAttribute("selectedService", serviceId);
    m.addAttribute("appointmentId", appointmentId);
    if (petId != null && vetId != null && serviceId != null && date != null) {
      long selected = petId;
      s.require(
          pets.stream().anyMatch(x -> s.number(x, "id") == selected),
          "Selecciona una mascota de tu cuenta.");
      m.addAttribute("slots", agenda.available(date, petId, vetId, serviceId));
    }
    return "booking";
  }

  @PostMapping("/reservar")
  public String reserve(
      Principal p,
      @RequestParam long petId,
      @RequestParam long vetId,
      @RequestParam long serviceId,
      @RequestParam LocalDateTime startsAt,
      RedirectAttributes r) {
    citas.reserve(actor(p), new Slot(petId, vetId, serviceId, startsAt));
    return ok(r, "Cita reservada. Te esperamos en la clínica.", "/citas");
  }

  @PostMapping("/citas/{id}/reprogramar")
  public String reschedule(
      Principal p,
      @PathVariable long id,
      @RequestParam long petId,
      @RequestParam long vetId,
      @RequestParam long serviceId,
      @RequestParam LocalDateTime startsAt,
      RedirectAttributes r) {
    citas.reschedule(actor(p), id, new Slot(petId, vetId, serviceId, startsAt));
    return ok(r, "Cita reprogramada.", "/citas");
  }

  @PostMapping("/citas/{id}/cancelar")
  public String cancel(Principal p, @PathVariable long id, RedirectAttributes r) {
    citas.cancel(actor(p), id);
    return ok(r, "Cita cancelada.", "/citas");
  }

  @PostMapping("/citas/{id}/llegada")
  public String checkin(Principal p, @PathVariable long id, RedirectAttributes r) {
    citas.checkin(actor(p), id);
    return ok(r, "Llegada confirmada. La cita está en espera.", "/citas");
  }

  @PostMapping("/citas/{id}/inasistencia")
  public String noShow(Principal p, @PathVariable long id, RedirectAttributes r) {
    citas.noShow(actor(p), id);
    return ok(r, "Inasistencia registrada.", "/citas");
  }

  @GetMapping("/citas/{id}/atender")
  public String attend(Principal p, @PathVariable long id, Model m) {
    var a = actor(p);
    var c =
        s.one(
            "select a.*,p.name pet_name from appointment a join pet p on p.id=a.pet_id where"
                + " a.id=?",
            id);
    s.require(
        a.vet() && a.id() == s.number(c, "vet_id"), "La cita está asignada a otro veterinario.");
    s.require(c.get("status").equals("EN_ESPERA"), "La cita debe estar en espera.");
    m.addAttribute("appointment", c);
    return "attend";
  }

  @PostMapping("/citas/{id}/atender")
  public String attend(
      Principal p,
      @PathVariable long id,
      @RequestParam String reason,
      @RequestParam String diagnosis,
      @RequestParam String treatment,
      @RequestParam BigDecimal weight,
      RedirectAttributes r) {
    citas.attend(actor(p), id, new Visit(reason, diagnosis, treatment, weight));
    return ok(r, "Atención registrada e historial actualizado.", "/citas");
  }

  @GetMapping("/historial")
  public String history(Principal p, Model m, @RequestParam(required = false) Long petId) {
    var a = actor(p);
    var pets = s.pets(a);
    if (a.vet())
      pets =
          pets.stream()
              .filter(
                  x ->
                      s.jdbc()
                              .queryForObject(
                                  "select count(*) from appointment where pet_id=? and vet_id=?",
                                  Integer.class,
                                  s.number(x, "id"),
                                  a.id())
                          > 0)
              .toList();
    m.addAttribute("pets", pets);
    m.addAttribute("selectedPet", petId);
    if (petId != null) m.addAttribute("records", citas.history(a, petId));
    return "history";
  }

  @GetMapping("/admin")
  public String admin(Model m) {
    m.addAttribute(
        "users",
        s.jdbc().queryForList("select id,name,email,role,active from app_user order by id"));
    m.addAttribute("services", s.jdbc().queryForList("select * from clinical_service order by id"));
    m.addAttribute(
        "hours", s.jdbc().queryForList("select * from business_hours order by day_of_week"));
    m.addAttribute(
        "holidays", s.jdbc().queryForList("select * from holiday order by holiday_date"));
    m.addAttribute(
        "audit",
        s.jdbc()
            .queryForList(
                "select e.*,u.name actor_name from audit_event e left join app_user u on"
                    + " u.id=e.actor_id order by e.id desc limit 25"));
    return "admin";
  }

  @PostMapping("/admin/usuarios")
  public String user(
      Principal p,
      @RequestParam String name,
      @RequestParam String email,
      @RequestParam String password,
      @RequestParam String dni,
      @RequestParam String phone,
      @RequestParam String role,
      RedirectAttributes r) {
    s.require(
        Set.of("ADMIN", "RECEPCION", "VETERINARIO").contains(role), "Selecciona un rol interno.");
    long id = users.register(name, email, password, dni, phone, true, role);
    s.audit(actor(p), "CREAR_USUARIO", id, clock);
    return ok(r, "Usuario interno creado.", "/admin");
  }

  @PostMapping("/admin/usuarios/{id}")
  public String active(
      Principal p, @PathVariable long id, @RequestParam boolean active, RedirectAttributes r) {
    users.setActive(actor(p), id, active);
    return ok(r, "Estado del usuario actualizado.", "/admin");
  }

  @PostMapping("/admin/servicios")
  public String service(
      @RequestParam(required = false) Long id,
      @RequestParam String name,
      @RequestParam(defaultValue = "false") boolean active,
      RedirectAttributes r) {
    s.require(
        !name.isBlank() && name.length() <= 80, "El servicio admite entre 1 y 80 caracteres.");
    if (id == null)
      s.jdbc()
          .update("insert into clinical_service(name,active) values (?,?)", name.strip(), active);
    else
      s.jdbc()
          .update(
              "update clinical_service set name=?,active=? where id=?", name.strip(), active, id);
    return ok(r, "Servicio guardado.", "/admin");
  }

  @PostMapping("/admin/horarios")
  public String hours(
      @RequestParam int day,
      @RequestParam LocalTime opening,
      @RequestParam LocalTime closing,
      @RequestParam(defaultValue = "false") boolean active,
      RedirectAttributes r) {
    s.require(
        day >= 1
            && day <= 7
            && opening.isBefore(closing)
            && opening.getMinute() % 30 == 0
            && closing.getMinute() % 30 == 0
            && opening.getSecond() == 0
            && closing.getSecond() == 0,
        "El horario debe tener bloques de 30 minutos y apertura anterior al cierre.");
    s.jdbc()
        .update(
            "update business_hours set opening=?,closing=?,active=? where day_of_week=?",
            opening,
            closing,
            active,
            day);
    return ok(r, "Horario actualizado; las citas existentes se conservan.", "/admin");
  }

  @PostMapping("/admin/feriados")
  public String holiday(
      @RequestParam LocalDate day, @RequestParam String description, RedirectAttributes r) {
    s.require(
        !description.isBlank() && description.length() <= 120,
        "Describe el feriado (máximo 120 caracteres).");
    s.jdbc().update("insert into holiday values (?,?)", day, description.strip());
    return ok(r, "Feriado registrado; las citas existentes se conservan.", "/admin");
  }

  @PostMapping("/admin/feriados/eliminar")
  public String holidayDelete(@RequestParam LocalDate day, RedirectAttributes r) {
    s.jdbc().update("delete from holiday where holiday_date=?", day);
    return ok(r, "Feriado eliminado.", "/admin");
  }

  @GetMapping("/admin/apariencia")
  public String appearance(Model m) {
    m.addAttribute("theme", themes.current());
    return "appearance";
  }

  @PostMapping("/admin/apariencia")
  public String appearance(
      Principal p, @RequestParam Map<String, String> input, RedirectAttributes r) {
    themes.save(actor(p), input);
    return ok(r, "Apariencia guardada para toda la clínica.", "/admin/apariencia");
  }

  @PostMapping("/admin/apariencia/restablecer")
  public String reset(Principal p, RedirectAttributes r) {
    themes.save(actor(p), ThemeService.DEFAULTS);
    return ok(r, "Apariencia original restaurada.", "/admin/apariencia");
  }

  @GetMapping("/admin/reportes")
  public String reports(
      Model m,
      @RequestParam(required = false) LocalDate from,
      @RequestParam(required = false) LocalDate to,
      @RequestParam(defaultValue = "") String state,
      @RequestParam(defaultValue = "0") long vetId,
      @RequestParam(defaultValue = "0") long serviceId) {
    if (from == null) from = LocalDate.now(clock).withDayOfMonth(1);
    if (to == null) to = LocalDate.now(clock).withDayOfMonth(LocalDate.now(clock).lengthOfMonth());
    s.require(
        !to.isBefore(from) && java.time.temporal.ChronoUnit.DAYS.between(from, to) <= 366,
        "Selecciona un rango de hasta 366 días.");
    var rows =
        s.jdbc()
            .queryForList(
                "select a.status,count(*) total from appointment a where starts_at>=? and"
                    + " starts_at<? and (?='' or a.status=?) and (?=0 or a.vet_id=?) and (?=0 or"
                    + " a.service_id=?) group by a.status order by a.status",
                from.atStartOfDay(),
                to.plusDays(1).atStartOfDay(),
                state,
                state,
                vetId,
                vetId,
                serviceId,
                serviceId);
    long total = rows.stream().mapToLong(x -> s.number(x, "total")).sum();
    long noShows =
        rows.stream()
            .filter(x -> x.get("status").equals("NO_ASISTIO"))
            .mapToLong(x -> s.number(x, "total"))
            .sum();
    m.addAttribute("rows", rows);
    m.addAttribute("total", total);
    m.addAttribute(
        "rate", total == 0 ? "0.0" : String.format(Locale.ROOT, "%.1f", 100.0 * noShows / total));
    m.addAttribute("from", from);
    m.addAttribute("to", to);
    m.addAttribute("vets", s.vets());
    m.addAttribute("services", s.services());
    return "reports";
  }
}
