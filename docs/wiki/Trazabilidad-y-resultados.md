# Trazabilidad y resultados

## Requisitos y pruebas

La [ERS](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/ERS.md) registra 12 RF y 6 RNF; la [matriz técnica](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/TRAZABILIDAD.md) enlaza función, ruta, componente y pruebas. El estado vivo se consulta en [Issues](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues). Las exportaciones reflejan un corte histórico, no una sincronización automática.

| Evidencia | Resultado documentado | Alcance y limitación |
|---|---|---|
| JUnit posterior a DF-01 | 44 aprobados; 0 fallos, errores u omitidos | Spring/H2, HTTP, concurrencia y PostgreSQL temporal. |
| JaCoCo histórico | 310/329 líneas = 94,22 %; ramas 111/165 = 67,27 % | Paquete service con Store; ejecución anterior de 43 casos. |
| Selenium | 11 aprobados; XML 28,557 s según informe | Chrome y ancho 360 px; XML pendiente de incorporar; Firefox pendiente. |
| JMeter recibido | 46 485 muestras; 0 errores, incluida humo | H2 en memoria y loopback; no prueba producción. |
| SVN | Ocho revisiones, rama, conflicto y recuperación según informe | Originales por incorporar; etiqueta final pendiente. |
| PILAR | Proyecto, inventario y amenazas según informe | Supuestos académicos; tratamientos y catálogo requieren revisión. |

## Resultados históricos de rendimiento

| Escenario | Máximo de hilos | Muestras | p95 disponibilidad (ms) | p95 reserva (ms) | Errores |
|---|---:|---:|---:|---:|---:|
| Humo | 2 | 72 | 106 | 62 | 0 |
| Carga | 10 | 4 524 | 59 | 77 | 0 |
| Estrés | 50 | 22 697 | 121 | 69 | 0 |
| Pico | 50 | 6 481 | 74 | 53 | 0 |
| Estabilidad | 10 | 12 711 | 86 | 122 | 0 |

Fuente: Informe_Avance_VetAgenda.docx, corte 24/11/2026, análisis de los JTL recibidos. Carga excluye los primeros 30 s; otros perfiles incluyen transiciones. Un registro JMeter es una muestra/transacción, no un usuario distinto ni necesariamente una sola solicitud HTTP. No se ejecutaron pruebas nuevas para publicar esta documentación.

## RNF y comprobaciones pendientes

| ID | Evaluación del avance | Para cierre |
|---|---|---|
| RNF-01 | Umbral cumplido en H2 local según informe | Incorporar originales, comparación antes/después y corregir monitoreo del generador. |
| RNF-02 | Controles concretos aprobados | Revisar inventario completo de rutas y tratamiento de riesgos; no equivale a auditoría integral. |
| RNF-03 | Sin medición de usuarios | Comprobar ≥80 % reservando en ≤2 min y ≤5 pasos. |
| RNF-04 | Persistencia configurada y scripts disponibles | Medir tres reinicios sin pérdida y restauración ≤10 min. |
| RNF-05 | Meta de líneas superada en reporte histórico | Conservar alcance y repetir si cambia paquete de negocio. |
| RNF-06 | Chrome y 360 px documentados | Verificar Firefox y versión final. |

## Cambios y defectos

CC-03: personalización implementada y probada. CC-04: ampliación de ADMIN diferida; no implementada. DF-01: lectura del color corregida con theme.get(key) y regresión aprobada. DF-02: interacción Selenium ajustada conservando las aserciones. El defecto controlado del límite de reprogramación produjo un fallo real antes de restaurar el código.

## Dictamen

**Apto con observaciones para demostración local de VetAgenda 0.1.0.** Los resultados respaldan reglas y flujos específicos. El cierre integral requiere completar requisitos documentales y mediciones y repetir lo afectado sobre la versión final identificada. El estado de una issue, la existencia de una página o un porcentaje de cobertura no acreditan por sí solos la entrega completa.

[Inicio](Home) · [G07](G07-Pruebas-unitarias) · [G08](G08-Pruebas-de-rendimiento) · [G09](G09-Gestion-de-calidad)
