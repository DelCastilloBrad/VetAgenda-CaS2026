# Trazabilidad de la implementación

Base: primer entregable de VetAgenda adjunto al proyecto. La tabla identifica implementación y evidencia de esta versión; no sustituye la ERS validada con el cliente.

| RF | Función y ruta principal | Implementación | Evidencia |
|---|---|---|---|
| 01 | Login, logout, bloqueo temporal | SecurityConfig, ActiveAccountFilter | WebTests: acceso, bloqueo, CSRF y revocación |
| 02 | Autorregistro `/registro` | UsuarioService.register | BusinessTests: consentimiento, DNI, correo, contraseña y BCrypt |
| 03 | Mascotas `/mascotas` | UsuarioService.savePet | BusinessTests: propiedad y fecha; WebTests: renderizado |
| 04 | Disponibilidad `/disponibilidad` | AgendaService | BusinessTests: días, límites, bloques, feriados y cierre |
| 05 | Reserva `/reservar` | CitaService.reserve | BusinessTests y ConcurrencyTests; prueba PostgreSQL real |
| 06 | Cancelación y reprogramación | CitaService.cancel/reschedule | BusinessTests: plazo, liberación y máximo de 2 cambios |
| 07 | Llegada `/citas/{id}/llegada` | CitaService.checkin | BusinessTests: rol, día y estado |
| 08 | Atención `/citas/{id}/atender` | CitaService.attend | BusinessTests: asignación, peso, estado y ficha |
| 09 | Inasistencia `/citas/{id}/inasistencia` | CitaService.noShow, InasistenciaService | BusinessTests: minuto 15, ventana de 90 días y excepción por recepción |
| 10 | Agenda `/citas`, historial `/historial` | Store, CitaService.history | BusinessTests: historial; WebTests: plantillas y acceso |
| 11 | Reporte consolidado `/admin/reportes` | WebController.reports | WebTests: permisos y renderizado; captura de revisión visual recibida; combinación completa de filtros pendiente |
| 12 | Usuarios, servicios, horarios, feriados `/admin` | WebController, UsuarioService | WebTests: acceso/renderizado; comprobación manual completa pendiente |

| RNF | Estado de evidencia |
|---|---|
| 01 Rendimiento | Pendiente ejecutar y analizar los cuatro escenarios JMeter en el entorno objetivo |
| 02 Seguridad | Contraseñas BCrypt, rutas por rol, CSRF, bloqueo y desactivación de sesión probados; no es auditoría integral |
| 03 Usabilidad | Pendiente sesión con usuarios; no se afirma el objetivo del 80 % / 2 minutos |
| 04 Persistencia y recuperación | Perfiles persistentes y scripts incluidos; faltan los tres reinicios medidos y restauración cronometrada |
| 05 Mantenibilidad | Reporte JaCoCo adjunto; ver cifra exacta en VALIDACION.md |
| 06 Portabilidad | CSS adaptable implementado; Chrome y 360 px comprobados en la suite del usuario; Firefox pendiente |

## Cambio solicitado en esta conversación

**CC-03 — Estilos reemplazables y personalizables.** Solicitud del usuario del 24/11/2026: facilitar cambios visuales sin afectar el resto del sistema. Se implementaron variables de tema, archivo externo con prioridad final, panel ADMIN con validación y restauración, y separación entre estilos y selectores funcionales. `BusinessTests.themeValidatesValuesAndRoles` comprueba permisos, rechazo de valores inválidos, persistencia y restauración. La suite Selenium incluye guardar y restablecer el tema, aprobada en la regresión del usuario (11 casos, 28,55 s).

La segunda solicitud real es CC-04 (ampliar operaciones del administrador), diferida expresamente por el usuario. Véase CAMBIOS.md. Los candidatos CC-01/CC-02 del primer entregable (limpieza y lista de espera) conservan sus identificadores y no se declaran implementados. El anterior alias local CC-01 para estilos se sustituye aquí por CC-03 para evitar colisiones.
