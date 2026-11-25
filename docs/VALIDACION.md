# Validación de VetAgenda 0.1.0

Fecha: 24 de noviembre de 2026 (America/Lima). Ejecución con JDK 21.0.13.1, Maven 3.9.9, Spring Boot 3.4.13, H2 2.3.232 y PostgreSQL real temporal 14.22. Los datos empleados son ficticios.

| Comprobación | Resultado |
|---|---|
| Reglas de negocio, datos, propiedad y tema | 31 casos JUnit aprobados |
| Autenticación, bloqueo, CSRF, rutas por rol, revocación y plantillas | 11 casos JUnit aprobados |
| Dos reservas simultáneas del mismo bloque | 1 caso aprobado; una sola reserva guardada |
| Flyway, reserva y cancelación con PostgreSQL real | 1 caso aprobado |
| Regresión total | **44 casos, 0 fallos, 0 errores, 0 omitidos** |
| Cobertura de líneas de `pe.unsa.vetagenda.service` | **310 cubiertas / 329 totales = 94,22 %** |
| Defecto temporal: aceptar 3 reprogramaciones | La prueba falló; fuente restaurado y regresión aprobada |
| Selenium | 11 casos aprobados en el equipo del usuario en 28,55 s; captura de consola adjunta; XML final pendiente de incorporar |
| Revisión visual en navegador / 360 px / Firefox | Chrome 158.0.8127.88 y ancho 360 px incluidos en la suite aprobada del usuario; Firefox pendiente |
| JMeter: carga, estrés, pico y estabilidad | Pendiente |
| Respaldo y restauración cronometrados | Scripts entregados; ejecución pendiente en el equipo final |

Las pruebas de negocio de esta entrega utilizan Spring y una base temporal: son pruebas automatizadas de componentes con integración, no se presentan como unidades totalmente aisladas. El reporte de cobertura adjunto corresponde a la ejecución inicial de 43 casos; las correcciones posteriores afectan una plantilla y la interacción Selenium, sin cambiar las clases de servicios. El JSON y hash del JAR inicial se conservan como registros históricos, no como resumen del código corregido. La cobertura mide líneas ejecutadas; no garantiza cobertura completa de caminos, rendimiento ni ausencia de defectos.

## Evidencia reproducible

- `evidencias/junit/`: reportes iniciales JUnit/Surefire (43 casos).
- `evidencias/DF-01/`: regresión posterior a corregir Apariencia (44 casos).
- `evidencias/selenium-consola-usuario.png`: captura suministrada por el usuario, 11 casos aprobados en 28,55 s. El XML final no se ha recibido.
- `evidencias/cobertura/index.html`: reporte JaCoCo navegable, fuentes y CSV/XML.
- `evidencias/defecto-controlado.log` y `.json`: fallo real del límite alterado y confirmación de restauración.
- `evidencias/regresion-final.log`: ejecución completa posterior a la restauración.
- `evidencias/resultados.json`: recuentos y SHA-256 del ejecutable.
- `evidencias/selenium-intento-bloqueado.xml`: intento real fallido de iniciar el navegador; no es evidencia de aprobación de la UI.

Comando de regresión: `mvn clean verify`. Para reproducir el defecto sin arriesgar la copia vigente, crea una copia de trabajo, cambia temporalmente `< 2` por `< 3` en el límite de `CitaService.reschedule`, ejecuta `mvn -Dtest=BusinessTests#enforcesTwoReschedules test`, restaura el archivo y vuelve a ejecutar `mvn verify`.

## Dictamen de esta entrega técnica

**Avance ejecutable con observaciones.** Se comprobaron las reglas y controles indicados. La aceptación completa del entregable académico requiere incorporar el XML Selenium final, ejecutar JMeter, las pruebas de recuperación y las demás actividades del enunciado en el entorno final. No se afirma cumplimiento medido de los RNF pendientes ni cierre de G01–G09.

No se han inventado entrevistas, validaciones del cliente, revisiones SVN, resultados de PILAR, mediciones de carga, capturas de navegador ni evidencias de reuniones. Los archivos de seguimiento reflejan únicamente trabajo comprobado en esta implementación.
