package pe.unsa.vetagenda.domain;

import java.time.*;

public final class Domain {
  private Domain() {}

  public record Actor(long id, String name, String email, String role) {
    public boolean client() {
      return role.equals("CLIENTE");
    }

    public boolean reception() {
      return role.equals("RECEPCION");
    }

    public boolean vet() {
      return role.equals("VETERINARIO");
    }

    public boolean admin() {
      return role.equals("ADMIN");
    }
  }

  public record Slot(long petId, long vetId, long serviceId, LocalDateTime startsAt) {}

  public record Visit(
      String reason, String diagnosis, String treatment, java.math.BigDecimal weight) {}

  public static class RuleViolation extends RuntimeException {
    public RuleViolation(String message) {
      super(message);
    }
  }
}
