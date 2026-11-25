# G08 · Pruebas de rendimiento

**Producto:** VetAgenda 0.1.0 · **Estado:** avance con pendientes de cierre · [Seguimiento G08](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/30)

## Objetivo

Contrastar disponibilidad y reserva con RNF-01: p95 ≤ 2 s y errores ≤ 1 % en la carga esperada.

## Herramienta, versión y requisitos vinculados

Apache JMeter 5.6.3 y Java OpenJDK 21.0.13.1, según el informe y la ejecución recibida. Generador y aplicación compartieron equipo.

RF-04 disponibilidad y RF-05 reserva; perfil académico de carga, estrés, pico y estabilidad.

## Qué exige el trabajo final

- Definir al menos dos transacciones, mezcla, autenticación, datos, validaciones y pausas.
- Registrar equipo, red, base, volumen, versión y configuración del generador.
- Ejecutar carga de 10 usuarios/5 min/rampa 30 s; estrés 10–25–50 por 3 min/nivel; pico 10→50→10; estabilidad 10/15 min, documentando ajustes.
- Conservar JMX, JTL y HTML en modo no gráfico y medir percentiles, throughput, errores, CPU y memoria.
- Justificar una mejora sobre un cuello de botella y comparar antes/después en condiciones equivalentes.

## Procedimiento realizado

El informe revisó cinco ejecuciones, incluida humo, y recalculó p95 y recuentos desde JTL. Cada ciclo consulta disponibilidad, reserva y cancela; hay 2 s de pausa y una autenticación por hilo. La reserva incluye POST y GET de confirmación.

## Resultados obtenidos

El informe acredita 46 485 muestras y cero errores en total. Carga: 4 524 muestras, p95 disponibilidad 59 ms y reserva 77 ms. Estrés: 22 697, 121/69 ms; pico: 6 481, 74/53 ms; estabilidad: 12 711, 86/122 ms; humo: 72, 106/62 ms. Son resultados históricos recibidos, cuyos originales todavía deben incorporarse al repositorio.

## Análisis

La base fue H2 en memoria y la red loopback. Carga excluye los primeros 30 s al resumir; los otros perfiles incluyen sus transiciones. Las cancelaciones permanecen almacenadas. Las cifras de CPU/RSS del generador probablemente siguieron al lanzador y deben excluirse del diagnóstico; 50 hilos no acreditan capacidad máxima.

## Conclusiones

RNF-01 cumple en el entorno local medido, según el informe. G08 permanece abierto: faltan mejora justificada, repetición equivalente y monitoreo correcto de la JVM de JMeter.

## Ubicación de evidencias

- [Informe y paquetes originales pendientes de incorporar](Evidencias-y-entregables)
- [Umbral RNF-01](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/ERS.md)
- [Disponibilidad y concurrencia](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/ARQUITECTURA.md)

## Pendientes y limitaciones

- [ ] Incorporar JMX, JTL, HTML, perfiles, datos y monitoreo de la ejecución antes.
- [ ] Corregir identificación de la JVM del generador y repetir medición.
- [ ] Aplicar una mejora sustentada y repetir con entorno, datos, versión y perfil equivalentes.
- [ ] Actualizar trazabilidad y gestor con el alcance local del resultado.

## Criterio de cierre

Umbrales contrastados por transacción y comparación antes/después verificable; conclusiones limitadas al entorno e intervalo medidos.

## Cuestionarios y referencias

Los cuestionarios específicos de PRAC01–PRAC09 deben contrastarse con cada guía original. Esta página organiza la aplicación al producto desde la consigna integradora; no declara resueltos cuestionarios cuyo texto no se ha verificado. Véase [Fuentes y criterios](Fuentes-y-criterios).

[Inicio](Home) · [Evidencias y entregables](Evidencias-y-entregables) · [Trazabilidad y resultados](Trazabilidad-y-resultados)
