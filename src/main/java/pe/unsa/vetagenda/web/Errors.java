package pe.unsa.vetagenda.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.unsa.vetagenda.domain.Domain.RuleViolation;

@ControllerAdvice
public class Errors {
  @ExceptionHandler(RuleViolation.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String rule(RuleViolation e, Model m) {
    m.addAttribute("message", e.getMessage());
    return "problem";
  }

  @ExceptionHandler({
    org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,
    org.springframework.web.bind.MissingServletRequestParameterException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String input(Exception e, Model m) {
    m.addAttribute("message", "Revisa los campos del formulario y sus formatos.");
    return "problem";
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public String duplicate(Exception e, Model m) {
    m.addAttribute(
        "message",
        "No se pudo guardar: existe un registro duplicado o relacionado. Revisa los datos.");
    return "problem";
  }
}
