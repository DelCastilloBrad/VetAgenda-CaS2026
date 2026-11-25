# G03 · Análisis y diseño

**Producto:** VetAgenda 0.1.0 · **Estado:** avance con pendientes de cierre · [Seguimiento G03](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/25)

## Objetivo

Explicar la estructura, comportamiento, datos e interfaz de VetAgenda mediante un SDD coherente con requisitos y código.

## Herramienta, versión y requisitos vinculados

Documentación Markdown y diagramas Mermaid existentes; la entrega pide fuentes editables UML y mockups. IEEE 1016 se usa como referencia académica del SDD.

Los tres procesos del producto y los RF-01 a RF-12; transacciones de reserva, estados de cita, permisos y ficha clínica.

## Procedimiento realizado

Se documentó un monolito MVC con Thymeleaf, servicios, JDBC transaccional y base relacional. Se describieron responsabilidades, estados de citas, bloqueo de veterinario y mascota y separación de tema visual.

## Resultados obtenidos

ARQUITECTURA.md contiene arquitectura y estados editables en Mermaid. El código tiene servicios de agenda, citas, usuarios, inasistencias y tema; las migraciones definen el esquema.

## Análisis

Los nombres de clases, tablas y rutas permiten rastreo. Falta formalizar los tres casos de uso y los modelos exigidos: una pantalla implementada no sustituye por sí sola un mockup y un esquema de componentes no acredita todos los diagramas UML.

## Conclusiones

La documentación describe la arquitectura y el comportamiento principal de VetAgenda y permite relacionarlos con el código.

## Ubicación de evidencias

- [Arquitectura y estados](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/ARQUITECTURA.md)
- [Requisitos hacia código](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/TRAZABILIDAD.md)
- [Esquema relacional](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/src/main/resources/db/migration/V1__schema.sql)
- [Pantallas implementadas](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/tree/main/src/main/resources/templates)

## Pendientes y limitaciones

- [ ] Completar el SDD y revisar la consistencia y trazabilidad de los tres procesos con requisitos e implementación.
- [ ] Añadir fuentes UML editables de casos de uso, clases y comportamiento, con exportaciones legibles.
- [ ] Documentar tres casos de uso y sus mockups.

[Inicio](Home) · [Evidencias y entregables](Evidencias-y-entregables) · [Trazabilidad y resultados](Trazabilidad-y-resultados) · [Fuentes técnicas](Fuentes-y-criterios)
