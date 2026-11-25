# Defectos observados

## DF-01 — El selector de color pierde el valor al recargar

- Versión detectada: VetAgenda 0.1.0.
- Caso: Selenium `test_09_theme_persists_and_resets`.
- Evidencia recibida: terminal del usuario con 8 casos aprobados y 1 fallo; la suite se detuvo antes de los casos 10 y 11.
- Pasos: administrador cambia color a `#315e9d`, guarda y recarga Apariencia.
- Esperado: el selector conserva el color guardado y permite restablecer el predeterminado.
- Obtenido: el CSS conserva el color, pero el HTML del selector devuelve `value=""`; Chrome muestra `#000000`.
- Impacto: menor, sobre la personalización visual. Una nueva edición podía reenviar colores negros en campos que debían conservarse.
- Causa localizada: la expresión dinámica `theme[key]` no recupera la entrada esperada en esta plantilla; la lectura explícita `theme.get(key)` conserva el valor.
- Corrección: una expresión en `templates/appearance.html`. No se modifican reglas de negocio ni permisos.
- Verificación de servidor: reproducción HTTP antes y después; color inicial, guardado y recargado correctos tras el cambio. Nueva regresión Spring MVC sobre guardar, recargar y restablecer.
- Regresión posterior a la corrección: 44 casos JUnit aprobados (incluida la nueva comprobación de guardar, recargar y restablecer). Reportes en `evidencias/DF-01/`.
- Cierre: verificado por la regresión completa del usuario: 11 casos aprobados en 28,55 s. Captura en `evidencias/selenium-consola-usuario.png`; XML final pendiente de incorporar.

El corrector `corregir_apariencia.py` reemplaza esa misma expresión en el JAR y en la plantilla fuente disponible, conserva copia del JAR anterior y registra los hashes. No modifica la base de datos ni las aserciones Selenium.

## DF-02 — Clic interceptado en la automatización de reprogramación

- Evidencia recibida: 10 casos Selenium aprobados y fallo del caso 11 al pulsar `confirm-booking`, con `ElementClickInterceptedException` en Chrome 158.0.8127.88.
- Alcance observado: fallo de interacción del navegador antes de la comprobación del resultado de reprogramación; la traza no identifica por sí sola el elemento que intercepta el clic.
- Ajuste de automatización: centrar el control en el área visible, esperar a que su centro esté libre mediante `elementFromPoint` y usar el clic nativo de WebDriver.
- Se conservan los 11 casos y todas las aserciones anteriores. No se ejecuta el clic mediante JavaScript ni se modifica el servidor.
- Comprobaciones locales: sintaxis Python válida y comparación AST de aserciones sin cambios.
- Cierre: verificado por la regresión completa del usuario: 11 casos aprobados en 28,55 s, incluido el caso 11. La evidencia disponible es la captura de consola, no el XML final.
