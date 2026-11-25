package pe.unsa.vetagenda;

import static org.assertj.core.api.Assertions.*;

import java.time.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pe.unsa.vetagenda.domain.Domain.*;
import pe.unsa.vetagenda.service.*;

@SpringBootTest
@ActiveProfiles("test")
class ConcurrencyTests {
  @Autowired Store s;
  @Autowired CitaService citas;
  @Autowired AgendaService agenda;
  @Autowired Clock clock;

  @Test
  void concurrentReservationsProduceOneWinner() throws Exception {
    var actor = s.actor("cliente@vetagenda.test");
    long pet = s.number(s.pets(actor).getFirst(), "id"),
        vet = s.number(s.vets().getFirst(), "id"),
        service = s.number(s.services().getFirst(), "id");
    var day = LocalDate.now(clock).plusDays(2);
    while (day.getDayOfWeek() == DayOfWeek.SUNDAY) day = day.plusDays(1);
    var slot = new Slot(pet, vet, service, day.atTime(15, 0));
    var gate = new CountDownLatch(1);
    try (var executor = Executors.newFixedThreadPool(2)) {
      Callable<Boolean> work =
          () -> {
            gate.await();
            try {
              citas.reserve(actor, slot);
              return true;
            } catch (RuleViolation e) {
              return false;
            }
          };
      var first = executor.submit(work);
      var second = executor.submit(work);
      gate.countDown();
      int successes =
          (first.get(20, TimeUnit.SECONDS) ? 1 : 0) + (second.get(20, TimeUnit.SECONDS) ? 1 : 0);
      assertThat(successes).isEqualTo(1);
      assertThat(
              s.jdbc()
                  .queryForObject(
                      "select count(*) from appointment where pet_id=? and starts_at=? and"
                          + " status='RESERVADA'",
                      Integer.class,
                      pet,
                      slot.startsAt()))
          .isEqualTo(1);
    } finally {
      s.jdbc()
          .update(
              "delete from audit_event where entity_id in (select id from appointment where"
                  + " pet_id=? and starts_at=?)",
              pet,
              slot.startsAt());
      s.jdbc()
          .update("delete from appointment where pet_id=? and starts_at=?", pet, slot.startsAt());
    }
  }
}
