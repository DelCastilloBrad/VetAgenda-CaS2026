package pe.unsa.vetagenda.service;

import java.time.*;
import org.springframework.stereotype.Service;

@Service
public class InasistenciaService {
  private final Store s;
  private final Clock clock;

  public InasistenciaService(Store s, Clock clock) {
    this.s = s;
    this.clock = clock;
  }

  public int count(long ownerId) {
    var now = LocalDateTime.now(clock);
    return s.jdbc()
        .queryForObject(
            "select count(*) from appointment a join pet p on p.id=a.pet_id where p.owner_id=? and"
                + " a.status='NO_ASISTIO' and a.starts_at>=? and a.starts_at<=?",
            Integer.class,
            ownerId,
            now.minusDays(90),
            now);
  }

  public void checkOnline(long ownerId) {
    s.require(
        count(ownerId) < 3,
        "Tienes 3 inasistencias en los últimos 90 días. Comunícate con recepción para reservar.");
  }
}
