package pe.unsa.vetagenda.web;

import java.io.IOException;
import java.nio.file.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import pe.unsa.vetagenda.service.ThemeService;

@RestController
public class ThemeController {
  private final ThemeService themes;
  private final Path directory;

  public ThemeController(
      ThemeService themes, @Value("${vetagenda.theme-directory}") String directory) {
    this.themes = themes;
    this.directory = Path.of(directory);
  }

  @GetMapping(value = "/theme/settings.css", produces = "text/css")
  public ResponseEntity<String> css() {
    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(themes.css());
  }

  @GetMapping(value = "/theme/custom.css", produces = "text/css")
  public ResponseEntity<String> custom() throws IOException {
    var path = directory.resolve("custom.css");
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(
            Files.isRegularFile(path)
                ? Files.readString(path)
                : "/* Sin personalizaciones externas. */");
  }
}
