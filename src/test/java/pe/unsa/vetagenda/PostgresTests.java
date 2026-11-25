package pe.unsa.vetagenda;

import static org.assertj.core.api.Assertions.*;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.time.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import pe.unsa.vetagenda.domain.Domain.*;
import pe.unsa.vetagenda.service.*;

@SpringBootTest
@ActiveProfiles("test")
class PostgresTests {
  static final EmbeddedPostgres pg;

  static {
    try {
      pg = EmbeddedPostgres.builder().setServerConfig("unix_socket_directories", "").start();
    } catch (java.io.IOException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  @DynamicPropertySource
  static void database(DynamicPropertyRegistry r) {
    r.add(
        "spring.datasource.url", () -> "jdbc:postgresql://localhost:" + pg.getPort() + "/postgres");
    r.add("spring.datasource.username", () -> "postgres");
    r.add("spring.datasource.password", () -> "postgres");
  }

  @Autowired Store s;
  @Autowired CitaService citas;
  @Autowired Clock clock;

  @Test
  void migrationAndReservationWorkOnRealPostgres() {
    var client = s.actor("cliente@vetagenda.test");
    long pet = s.number(s.pets(client).getFirst(), "id"),
        vet = s.number(s.vets().getFirst(), "id"),
        service = s.number(s.services().getFirst(), "id");
    var day = LocalDate.now(clock).plusDays(2);
    while (day.getDayOfWeek() == DayOfWeek.SUNDAY) day = day.plusDays(1);
    long id = citas.reserve(client, new Slot(pet, vet, service, day.atTime(11, 0)));
    assertThat(s.one("select status from appointment where id=?", id).get("status"))
        .isEqualTo("RESERVADA");
    citas.cancel(client, id);
    assertThat(s.one("select status from appointment where id=?", id).get("status"))
        .isEqualTo("CANCELADA");
  }

  @AfterAll
  static void stop() throws Exception {
    pg.close();
  }
}
