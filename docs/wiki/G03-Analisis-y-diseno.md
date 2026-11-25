# G03 · Análisis y diseño

**Producto:** VetAgenda 0.1.0 · **Estado:** avance con pendientes de cierre · [Seguimiento G03](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/25)

## Objetivo

Explicar la estructura, comportamiento, datos e interfaz de VetAgenda mediante un SDD coherente con requisitos y código.

## Herramienta, versión y requisitos vinculados

Documentación Markdown y diagramas Mermaid existentes; la entrega pide fuentes editables UML y mockups. IEEE 1016 se usa como referencia académica del SDD.

Los tres procesos del producto y los RF-01 a RF-12; transacciones de reserva, estados de cita, permisos y ficha clínica.

## Qué exige el trabajo final

- Elaborar SDD con interesados, preocupaciones, vistas y decisiones de diseño.
- Entregar diagramas de casos de uso, clases y al menos uno de comportamiento de un proceso crítico.
- Especificar al menos tres casos de uso con precondiciones, flujos, alternativas, excepciones y poscondiciones, más mockups.
- Incluir arquitectura, modelo de datos, tabla de utilidad de diagramas y revisión de cumplimiento del SDD.

## Procedimiento realizado

Se documentó un monolito MVC con Thymeleaf, servicios, JDBC transaccional y base relacional. Se describieron responsabilidades, estados de citas, bloqueo de veterinario y mascota y separación de tema visual.

## Resultados obtenidos

ARQUITECTURA.md contiene arquitectura y estados editables en Mermaid. El código tiene servicios de agenda, citas, usuarios, inasistencias y tema; las migraciones definen el esquema. Esto es una base técnica, no un SDD formal completo.

## Análisis

Los nombres de clases, tablas y rutas permiten rastreo. Falta formalizar los tres casos de uso y los modelos exigidos: una pantalla implementada no sustituye por sí sola un mockup y un esquema de componentes no acredita todos los diagramas UML.

## Conclusiones

La implementación tiene diseño documentado de base. G03 continúa pendiente de sus artefactos formales y de la revisión de consistencia.

## Ubicación de evidencias

- [Arquitectura y estados](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/ARQUITECTURA.md)
- [Requisitos hacia código](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/TRAZABILIDAD.md)
- [Esquema relacional](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/src/main/resources/db/migration/V1__schema.sql)
- [Pantallas implementadas](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/tree/main/src/main/resources/templates)

## Pendientes y limitaciones

- [ ] Completar SDD y revisión con criterios cumplidos, pendientes y evidencia.
- [ ] Añadir fuentes UML editables de casos de uso, clases y comportamiento, con exportaciones legibles.
- [ ] Documentar tres casos de uso y sus mockups.

## Criterio de cierre

El diseño explica los tres procesos y cada elemento relevante se rastrea hacia requisitos e implementación.

## Cuestionarios y referencias

Los cuestionarios específicos de PRAC01–PRAC09 deben contrastarse con cada guía original. Esta página organiza la aplicación al producto desde la consigna integradora; no declara resueltos cuestionarios cuyo texto no se ha verificado. Véase [Fuentes y criterios](Fuentes-y-criterios).

[Inicio](Home) · [Evidencias y entregables](Evidencias-y-entregables) · [Trazabilidad y resultados](Trazabilidad-y-resultados)
