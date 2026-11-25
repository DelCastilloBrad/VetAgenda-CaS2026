# VetAgenda · Calidad de Software

Documentación del trabajo final integrador de la Universidad Nacional de San Agustín de Arequipa, semestre 2026 B. Cada guía se aplica al mismo producto y conserva requisitos, procedimiento, resultados, análisis, conclusiones y evidencias.

## Producto y alcance

VetAgenda gestiona reserva y agenda, llegada y atención clínica, historial y administración para una clínica veterinaria pequeña. Tiene 12 RF, 6 RNF y cuatro roles: CLIENTE, RECEPCION, VETERINARIO y ADMIN. La versión de avance es **0.1.0**. Pagos, facturación, inventario y mensajería real quedan fuera del alcance. ADMIN no hereda operaciones de recepción: CC-04 sigue diferida.

## Equipo

- Del Castillo Montoya Christopher Brad.
- Huayhua Hillpa Yourdyy Yossimar.
- Huamani Ollachica David Alfredo.
- Quispe Madariaga Jeferson Jofre.

## Las nueve guías

| Página | Aplicación a VetAgenda | Seguimiento |
|---|---|---|
| [G01 · Requerimientos](G01-Requerimientos) | Definir una especificación de requisitos verificable para los tres procesos de VetAgenda: reserva y agenda, llegada y atención, consulta y administración. | [Issue #23](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/23) |
| [G02 · Proceso de requerimientos](G02-Proceso-de-requerimientos) | Reconstruir el recorrido de requisitos y cambios desde su registro hasta la verificación, con responsables, fechas y evidencia. | [Issue #24](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/24) |
| [G03 · Análisis y diseño](G03-Analisis-y-diseno) | Explicar la estructura, comportamiento, datos e interfaz de VetAgenda mediante un SDD coherente con requisitos y código. | [Issue #25](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/25) |
| [G04 · Dimensionamiento y estimación](G04-Dimensionamiento-y-estimacion) | Estimar tamaño funcional, esfuerzo, duración y costo del mismo alcance de VetAgenda de forma recalculable. | [Issue #26](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/26) |
| [G05 · Gestión de configuración](G05-Gestion-de-configuracion) | Reconstruir versiones del producto, controlar cambios e identificar qué versión produjo cada evidencia. | [Issue #27](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/27) |
| [G06 · Gestión de riesgos](G06-Gestion-de-riesgos) | Analizar activos, amenazas y salvaguardas del sistema que soporta VetAgenda y verificar tratamientos del producto. | [Issue #28](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/28) |
| [G07 · Pruebas unitarias y cobertura](G07-Pruebas-unitarias) | Comprobar reglas de negocio y regresiones de VetAgenda con aserciones ejecutables y cobertura cuyo alcance sea explícito. | [Issue #29](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/29) |
| [G08 · Pruebas de rendimiento](G08-Pruebas-de-rendimiento) | Contrastar disponibilidad y reserva con RNF-01: p95 ≤ 2 s y errores ≤ 1 % en la carga esperada. | [Issue #30](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/30) |
| [G09 · Gestión de calidad con Selenium](G09-Gestion-de-calidad) | Verificar los tres procesos mediante flujos web y emitir un dictamen sustentado en requisitos, riesgos, pruebas y rendimiento. | [Issue #31](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/31) |

## Consulta transversal

- [Trazabilidad, resultados y dictamen](Trazabilidad-y-resultados).
- [Evidencias, pendientes de incorporación y entrega final](Evidencias-y-entregables).
- [Fuentes y criterios de actualización](Fuentes-y-criterios).
- [Instalación y ejecución](https://github.com/DelCastilloBrad/VetAgenda-CaS2026#abrir-la-demostración).
- [Código y documentación](https://github.com/DelCastilloBrad/VetAgenda-CaS2026) · [Issues](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues).

## Estado del avance

El informe de avance al 24/11/2026 acredita 44 casos JUnit aprobados, cobertura histórica de líneas de servicios del 94,22 %, 11 Selenium aprobados en Chrome y cinco ejecuciones JMeter (incluida humo) con 46 485 muestras y cero errores. Las páginas distinguen evidencia publicada de archivos recibidos que todavía deben incorporarse al repositorio.

**Dictamen: apto con observaciones para demostración local.** Ninguna guía se declara cerrada solo por crear su página. Quedan pendientes, entre otros, elicitación y revisión formal, SDD/UML/mockups, estimación editable, trazabilidad de versión final y mediciones adicionales.

## Hitos de la consigna

| Fecha | Entrega |
|---|---|
| 11/11/2026 | Primer entregable: producto, necesidad y alcance. |
| 25/11/2026 | Segundo entregable: avance de construcción y pruebas. |
| 02/12/2026 | Tercer entregable y sustentación final. |

Las fechas identifican hitos previstos, no acreditan aprobación del docente. La consigna requiere además carpetas G01 a G09 con originales y fuentes editables: la Wiki funciona como índice y desarrollo navegable y debe enlazar esos archivos.
