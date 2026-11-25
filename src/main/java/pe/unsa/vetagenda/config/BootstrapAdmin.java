package pe.unsa.vetagenda.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.unsa.vetagenda.service.*;

@Component
@Order(100)
public class BootstrapAdmin implements ApplicationRunner {
  private final Store s;
  private final UsuarioService users;
  private final String password, email;
  private final boolean demo;

  public BootstrapAdmin(
      Store s,
      UsuarioService users,
      @Value("${BOOTSTRAP_PASSWORD:}") String password,
      @Value("${BOOTSTRAP_EMAIL:admin@vetagenda.local}") String email,
      @Value("${vetagenda.demo.enabled:false}") boolean demo) {
    this.s = s;
    this.users = users;
    this.password = password;
    this.email = email;
    this.demo = demo;
  }

  public void run(ApplicationArguments args) {
    if (!demo
        && !password.isEmpty()
        && s.jdbc()
                .queryForObject("select count(*) from app_user where role='ADMIN'", Integer.class)
            == 0)
      users.register("Administración", email, password, "00000000", "900000000", true, "ADMIN");
  }
}
