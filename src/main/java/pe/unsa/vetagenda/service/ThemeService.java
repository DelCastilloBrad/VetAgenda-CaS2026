package pe.unsa.vetagenda.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.unsa.vetagenda.domain.Domain.*;

@Service
public class ThemeService {
  public static final Map<String, String> DEFAULTS =
      Map.of(
          "primary",
          "#157b68",
          "background",
          "#f5f7f6",
          "surface",
          "#ffffff",
          "text",
          "#1e3430",
          "radius",
          "16",
          "scale",
          "100",
          "font",
          "system");
  private final Store s;

  public ThemeService(Store s) {
    this.s = s;
  }

  public Map<String, String> current() {
    var map = new HashMap<>(DEFAULTS);
    s.jdbc()
        .queryForList("select * from theme_setting")
        .forEach(r -> map.put((String) r.get("setting_key"), (String) r.get("setting_value")));
    return map;
  }

  @Transactional
  public void save(Actor actor, Map<String, String> input) {
    s.require(actor.admin(), "Solo administración puede personalizar el tema.");
    var values = new HashMap<String, String>();
    for (var key : DEFAULTS.keySet()) {
      String value = input.getOrDefault(key, DEFAULTS.get(key));
      if (Set.of("primary", "background", "surface", "text").contains(key))
        s.require(value.matches("#[0-9a-fA-F]{6}"), "Usa colores hexadecimales válidos.");
      if (key.equals("radius"))
        s.require(
            Set.of("0", "8", "16", "24").contains(value), "Selecciona un redondeado permitido.");
      if (key.equals("scale"))
        s.require(Set.of("100", "110", "120").contains(value), "Selecciona un tamaño permitido.");
      if (key.equals("font"))
        s.require(
            Set.of("system", "serif", "mono").contains(value),
            "Selecciona una tipografía permitida.");
      values.put(key, value);
    }
    s.jdbc().update("delete from theme_setting");
    values.forEach((k, v) -> s.jdbc().update("insert into theme_setting values (?,?)", k, v));
  }

  public String css() {
    var t = current();
    String primary = t.get("primary"),
        text = t.get("text"),
        surface = t.get("surface"),
        background = t.get("background");
    return ":root{--va-primary:"
        + primary
        + ";--va-on-primary:"
        + (luminance(primary) > .4 ? "#10251e" : "#ffffff")
        + ";--va-bg:"
        + background
        + ";--va-surface:"
        + surface
        + ";--va-text:"
        + text
        + ";--va-muted:"
        + text
        + ";--va-radius:"
        + t.get("radius")
        + "px;--va-font-scale:"
        + t.get("scale")
        + "%;--va-font:"
        + switch (t.get("font")) {
          case "serif" -> "Georgia,serif";
          case "mono" -> "ui-monospace,monospace";
          default -> "Inter,system-ui,sans-serif";
        }
        + ";}";
  }

  private double luminance(String h) {
    double r = Integer.parseInt(h.substring(1, 3), 16) / 255.0,
        g = Integer.parseInt(h.substring(3, 5), 16) / 255.0,
        b = Integer.parseInt(h.substring(5, 7), 16) / 255.0;
    return .2126 * r + .7152 * g + .0722 * b;
  }
}
