package pe.unsa.vetagenda.service;

import java.time.*;
import java.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.unsa.vetagenda.domain.Domain.*;

@Service
public class UsuarioService {
  private final Store s;
  private final PasswordEncoder encoder;
  private final Clock clock;

  public UsuarioService(Store s, PasswordEncoder encoder, Clock clock) {
    this.s = s;
    this.encoder = encoder;
    this.clock = clock;
  }

  @Transactional
  public long register(
      String name,
      String email,
      String password,
      String dni,
      String phone,
      boolean consent,
      String role) {
    s.require(
        name != null && !name.isBlank() && name.length() <= 100,
        "El nombre es obligatorio (máximo 100 caracteres).");
    s.require(
        email != null && email.length() <= 150 && email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"),
        "Ingresa un correo válido.");
    s.require(
        password != null
            && password.length() >= 10
            && password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length <= 72,
        "Usa una contraseña de 10 a 72 bytes.");
    s.require(dni != null && dni.matches("[0-9]{8}"), "El DNI debe tener 8 dígitos.");
    s.require(phone != null && phone.matches("[0-9]{9}"), "El teléfono debe tener 9 dígitos.");
    s.require(consent, "Debes aceptar el uso de datos para gestionar tus citas.");
    s.require(
        Set.of("CLIENTE", "RECEPCION", "VETERINARIO", "ADMIN").contains(role), "Rol inválido.");
    s.require(
        s.jdbc()
                .queryForObject(
                    "select count(*) from app_user where email=? or dni=?",
                    Integer.class,
                    email.strip().toLowerCase(Locale.ROOT),
                    dni)
            == 0,
        "El correo o DNI ya está registrado.");
    return s.insertId(
        "insert into app_user(name,email,password,dni,phone,role,consent) values (?,?,?,?,?,?,?)",
        name.strip(),
        email.strip().toLowerCase(Locale.ROOT),
        encoder.encode(password),
        dni,
        phone,
        role,
        true);
  }

  @Transactional
  public long savePet(
      Actor actor,
      Long id,
      long owner,
      String name,
      String species,
      String breed,
      LocalDate birth) {
    s.require(actor.client() || actor.reception(), "Tu rol no permite editar mascotas.");
    if (actor.client()) owner = actor.id();
    s.require(
        s.jdbc()
                .queryForObject(
                    "select count(*) from app_user where id=? and role='CLIENTE' and active=true",
                    Integer.class,
                    owner)
            == 1,
        "Selecciona un cliente activo.");
    s.require(
        name != null && !name.isBlank() && name.length() <= 80,
        "El nombre de la mascota es obligatorio (máximo 80).");
    s.require(
        Set.of("Perro", "Gato", "Ave", "Otro").contains(species), "Selecciona una especie válida.");
    s.require(breed != null && breed.length() <= 80, "La raza admite hasta 80 caracteres.");
    s.require(
        birth == null || !birth.isAfter(LocalDate.now(clock)),
        "La fecha de nacimiento no puede estar en el futuro.");
    if (id == null) {
      long result =
          s.insertId(
              "insert into pet(owner_id,name,species,breed,birth_date) values (?,?,?,?,?)",
              owner,
              name.strip(),
              species,
              breed.strip(),
              birth);
      s.audit(actor, "CREAR_MASCOTA", result, clock);
      return result;
    }
    var old = s.one("select * from pet where id=? for update", id);
    s.require(s.number(old, "owner_id") == owner, "El propietario de la mascota no puede cambiar.");
    s.jdbc()
        .update(
            "update pet set name=?,species=?,breed=?,birth_date=? where id=?",
            name.strip(),
            species,
            breed.strip(),
            birth,
            id);
    s.audit(actor, "EDITAR_MASCOTA", id, clock);
    return id;
  }

  @Transactional
  public void setActive(Actor actor, long id, boolean active) {
    s.require(actor.admin(), "Solo administración gestiona usuarios.");
    s.require(actor.id() != id, "No puedes desactivar tu propia cuenta.");
    s.one("select id from app_user where id=?", id);
    s.jdbc().update("update app_user set active=? where id=?", active, id);
    s.audit(actor, active ? "ACTIVAR_USUARIO" : "DESACTIVAR_USUARIO", id, clock);
  }
}
