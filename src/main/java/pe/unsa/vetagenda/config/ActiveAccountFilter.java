package pe.unsa.vetagenda.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.unsa.vetagenda.service.Store;

/** Re-check stored account status so deactivation also revokes an existing session. */
public class ActiveAccountFilter extends OncePerRequestFilter {
  private final Store store;

  public ActiveAccountFilter(Store store) {
    this.store = store;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null
        && auth.isAuthenticated()
        && !(auth instanceof AnonymousAuthenticationToken)
        && store
                .jdbc()
                .queryForObject(
                    "select count(*) from app_user where email=? and active=true",
                    Integer.class,
                    auth.getName())
            == 0) {
      SecurityContextHolder.clearContext();
      var session = request.getSession(false);
      if (session != null) session.invalidate();
      response.sendRedirect("/login?disabled");
      return;
    }
    chain.doFilter(request, response);
  }
}
