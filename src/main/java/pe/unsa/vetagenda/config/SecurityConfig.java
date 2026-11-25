package pe.unsa.vetagenda.config;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pe.unsa.vetagenda.service.Store;

@Configuration
public class SecurityConfig {
  @Bean
  public AuthenticationProvider authenticationProvider(
      Store s, PasswordEncoder encoder, Clock clock) {
    return new AuthenticationProvider() {
      private final String dummy = encoder.encode("constant-time-unknown-account");

      public Authentication authenticate(Authentication request) {
        String email = request.getName().strip().toLowerCase(Locale.ROOT);
        var rows = s.jdbc().queryForList("select * from app_user where email=?", email);
        if (rows.isEmpty()) {
          encoder.matches(request.getCredentials().toString(), dummy);
          throw new BadCredentialsException("Credenciales inválidas");
        }
        var u = rows.getFirst();
        long id = s.number(u, "id");
        var now = LocalDateTime.now(clock);
        var until = (Timestamp) u.get("locked_until");
        if (!(Boolean) u.get("active") || (until != null && until.toLocalDateTime().isAfter(now)))
          throw new BadCredentialsException("Credenciales inválidas o cuenta bloqueada");
        if (!encoder.matches(request.getCredentials().toString(), (String) u.get("password"))) {
          s.jdbc()
              .update(
                  "update app_user set failed_attempts=case when locked_until is not null and"
                      + " locked_until<=? then 1 else failed_attempts+1 end,locked_until=case when"
                      + " failed_attempts>=4 and (locked_until is null or locked_until>?) then ?"
                      + " else null end where id=?",
                  now,
                  now,
                  now.plusMinutes(15),
                  id);
          throw new BadCredentialsException("Credenciales inválidas");
        }
        s.jdbc().update("update app_user set failed_attempts=0,locked_until=null where id=?", id);
        return new UsernamePasswordAuthenticationToken(
            email, null, List.of(new SimpleGrantedAuthority("ROLE_" + u.get("role"))));
      }

      public boolean supports(Class<?> c) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(c);
      }
    };
  }

  @Bean
  public SecurityFilterChain security(
      HttpSecurity http, AuthenticationProvider provider, Store store) throws Exception {
    return http.authenticationProvider(provider)
        .addFilterAfter(
            new ActiveAccountFilter(store),
            org.springframework.security.web.context.SecurityContextHolderFilter.class)
        .authorizeHttpRequests(
            a ->
                a.requestMatchers(
                        "/login", "/registro", "/css/**", "/js/**", "/webjars/**", "/theme/**")
                    .permitAll()
                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN")
                    .requestMatchers(
                        "/mascotas/**",
                        "/reservar",
                        "/disponibilidad",
                        "/citas/*/cancelar",
                        "/citas/*/reprogramar")
                    .hasAnyRole("CLIENTE", "RECEPCION")
                    .requestMatchers("/citas/*/llegada", "/citas/*/inasistencia")
                    .hasRole("RECEPCION")
                    .requestMatchers("/citas/*/atender")
                    .hasRole("VETERINARIO")
                    .anyRequest()
                    .authenticated())
        .formLogin(
            f ->
                f.loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login?error")
                    .permitAll())
        .logout(l -> l.logoutSuccessUrl("/login?logout"))
        .headers(
            h ->
                h.contentSecurityPolicy(
                    c ->
                        c.policyDirectives(
                            "default-src 'self'; style-src 'self'; script-src 'self'; img-src"
                                + " 'self' data:; font-src 'self'; form-action 'self';"
                                + " frame-ancestors 'none'; base-uri 'self'")))
        .build();
  }
}
