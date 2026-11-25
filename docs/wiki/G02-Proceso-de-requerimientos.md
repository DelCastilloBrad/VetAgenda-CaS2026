# G02 · Proceso de requerimientos

**Producto:** VetAgenda 0.1.0 · **Estado:** avance con pendientes de cierre · [Seguimiento G02](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/24)

## Objetivo

Reconstruir el recorrido de requisitos y cambios desde su registro hasta la verificación, con responsables, fechas y evidencia.

## Herramienta, versión y requisitos vinculados

GitHub Issues como gestor equivalente de incidencias, plantillas Markdown y exportaciones CSV/JSON. No se exige un tablero Projects para que exista el historial.

Seguimiento detallado de RF-01, RF-04, RF-05, RF-06 y RF-08; solicitudes reales CC-03 y CC-04.

## Qué exige el trabajo final

- Definir el flujo registrado, analizado, aprobado/rechazado, implementado, verificado y cerrado.
- Recorrer el flujo con al menos cinco requisitos, conservando fecha, responsable y evidencia.
- Registrar dos cambios reales y analizar diseño, costo, plazo, riesgos, pruebas y versión; implementar al menos uno aprobado.
- Actualizar ERS, diseño, código y pruebas del cambio implementado, exportar seguimiento y comparar con una alternativa.

## Procedimiento realizado

Se importó la línea base existente con fechas reales del gestor. Cinco requisitos conservan comentarios y eventos de transición. CC-03 incorpora personalización visual; CC-04 propone operaciones de recepción para ADMIN y permanece diferida por decisión del usuario.

## Resultados obtenidos

El corte exportado de 25/11/2026 01:45:27 UTC (24/11/2026 20:45:27 Lima) registra 31 issues: 13 cerradas y 18 abiertas. Es una instantánea histórica. CC-03 se relaciona con ThemeService, ThemeController, la plantilla de apariencia y las regresiones; CC-04 no está implementada.

## Análisis

Los cierres técnicos no equivalen a aceptación del cliente. La importación no demuestra uso del gestor durante todo el desarrollo. CC-01 y CC-02 de la propuesta eran candidatos, no cambios ejecutados; los costos y horas no medidos deben seguir identificados como pendientes.

## Conclusiones

El recorrido técnico es consultable. El cierre de G02 requiere completar el impacto y revisar el seguimiento y la decisión del segundo cambio según la consigna.

## Ubicación de evidencias

- [Flujo y comparación de herramientas](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/GESTION_REQUISITOS.md)
- [Solicitudes CC-03 y CC-04](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/CAMBIOS.md)
- [Índice de Issues](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/SEGUIMIENTO.md)
- [Exportaciones históricas CSV/JSON](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/tree/main/docs/seguimiento)

## Pendientes y limitaciones

- [ ] Completar costo, plazo e impacto con supuestos explícitos o mediciones reales.
- [ ] Conservar CC-04 como diferida hasta una decisión posterior; no presentarla como aceptada, rechazada o implementada.
- [ ] Actualizar exportaciones cuando cambie el gestor y vincular versión y regresión de cada cierre.

## Criterio de cierre

Se reconstruyen cinco requisitos y un cambio implementado llega a una versión y una prueba verificable; la segunda solicitud tiene una decisión sustentada.

## Cuestionarios y referencias

Los cuestionarios específicos de PRAC01–PRAC09 deben contrastarse con cada guía original. Esta página organiza la aplicación al producto desde la consigna integradora; no declara resueltos cuestionarios cuyo texto no se ha verificado. Véase [Fuentes y criterios](Fuentes-y-criterios).

[Inicio](Home) · [Evidencias y entregables](Evidencias-y-entregables) · [Trazabilidad y resultados](Trazabilidad-y-resultados)
