package pe.unsa.vetagenda;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pe.unsa.vetagenda.service.Store;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class WebTests {
  @Autowired MockMvc mvc;
  @Autowired Store s;

  @Test
  void publicFormsRender() throws Exception {
    mvc.perform(get("/login")).andExpect(status().isOk());
    mvc.perform(get("/registro")).andExpect(status().isOk());
  }

  @Test
  void protectedRouteRequiresSession() throws Exception {
    mvc.perform(get("/citas")).andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(username = "cliente@vetagenda.test", roles = "CLIENTE")
  void allClientScreensRender() throws Exception {
    for (String path :
        new String[] {
          "/",
          "/citas",
          "/mascotas",
          "/historial",
          "/reservar",
          "/disponibilidad?petId=1&vetId=3&serviceId=1&date=2026-11-25"
        }) mvc.perform(get(path)).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin@vetagenda.test", roles = "ADMIN")
  void allAdminScreensRender() throws Exception {
    for (String path : new String[] {"/", "/admin", "/admin/apariencia", "/admin/reportes"})
      mvc.perform(get(path)).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "cliente@vetagenda.test", roles = "CLIENTE")
  void clientCannotOpenAdminOrCheckin() throws Exception {
    mvc.perform(get("/admin")).andExpect(status().isForbidden());
    mvc.perform(post("/citas/1/llegada").with(csrf())).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "cliente@vetagenda.test", roles = "CLIENTE")
  void csrfIsRequired() throws Exception {
    mvc.perform(post("/mascotas").param("name", "Luna")).andExpect(status().isForbidden());
  }

  @Test
  void correctAndIncorrectLogin() throws Exception {
    mvc.perform(
            post("/login")
                .with(csrf())
                .param("username", "cliente@vetagenda.test")
                .param("password", "incorrecta"))
        .andExpect(redirectedUrl("/login?error"));
    mvc.perform(
            post("/login")
                .with(csrf())
                .param("username", "cliente@vetagenda.test")
                .param("password", "DemoVet2026!"))
        .andExpect(redirectedUrl("/"));
  }

  @Test
  void locksAfterFiveAttempts() throws Exception {
    for (int i = 0; i < 5; i++)
      mvc.perform(
              post("/login")
                  .with(csrf())
                  .param("username", "cliente@vetagenda.test")
                  .param("password", "incorrecta"))
          .andExpect(redirectedUrl("/login?error"));
    mvc.perform(
            post("/login")
                .with(csrf())
                .param("username", "cliente@vetagenda.test")
                .param("password", "DemoVet2026!"))
        .andExpect(redirectedUrl("/login?error"));
    org.assertj.core.api.Assertions.assertThat(
            s.one("select locked_until from app_user where email=?", "cliente@vetagenda.test")
                .get("locked_until"))
        .isNotNull();
  }

  @Test
  void themeAssetsArePublicAndNoCache() throws Exception {
    mvc.perform(get("/theme/settings.css"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("text/css"))
        .andExpect(header().string("Cache-Control", "no-store"));
  }

  @Test
  @WithMockUser(username = "cliente@vetagenda.test", roles = "CLIENTE")
  void deactivationRevokesCurrentSession() throws Exception {
    s.jdbc().update("update app_user set active=false where email=?", "cliente@vetagenda.test");
    mvc.perform(get("/")).andExpect(redirectedUrl("/login?disabled"));
  }

  @Test
  @WithMockUser(username = "admin@vetagenda.test", roles = "ADMIN")
  void appearanceShowsStoredColorAfterReload() throws Exception {
    mvc.perform(
            post("/admin/apariencia").with(csrf()).param("primary", "#315e9d").param("radius", "8"))
        .andExpect(redirectedUrl("/admin/apariencia"));
    String html =
        mvc.perform(get("/admin/apariencia"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    java.util.regex.Matcher input =
        java.util.regex.Pattern.compile("<input\\b[^>]*id=\"primary\"[^>]*>").matcher(html);
    org.assertj.core.api.Assertions.assertThat(input.find()).isTrue();
    org.assertj.core.api.Assertions.assertThat(input.group()).contains("value=\"#315e9d\"");
    mvc.perform(post("/admin/apariencia/restablecer").with(csrf()))
        .andExpect(redirectedUrl("/admin/apariencia"));
    String reset =
        mvc.perform(get("/admin/apariencia"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    java.util.regex.Matcher restored =
        java.util.regex.Pattern.compile("<input\\b[^>]*id=\"primary\"[^>]*>").matcher(reset);
    org.assertj.core.api.Assertions.assertThat(restored.find()).isTrue();
    org.assertj.core.api.Assertions.assertThat(restored.group()).contains("value=\"#157b68\"");
  }
}
