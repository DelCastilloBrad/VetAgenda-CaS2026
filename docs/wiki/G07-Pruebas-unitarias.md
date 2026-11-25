# G07 · Pruebas unitarias y cobertura

**Producto:** VetAgenda 0.1.0 · **Estado:** avance con pendientes de cierre · [Seguimiento G07](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/29)

## Objetivo

Comprobar reglas de negocio y regresiones de VetAgenda con aserciones ejecutables y cobertura cuyo alcance sea explícito.

## Herramienta, versión y requisitos vinculados

Java 21, Maven 3.9.9 reportado, JUnit Jupiter 5.11.4 y JaCoCo 0.8.12. El proyecto usa dependencias de prueba administradas por Spring Boot 3.4.13.

AgendaService, CitaService, UsuarioService, InasistenciaService y ThemeService; el paquete medido pe.unsa.vetagenda.service incluye Store.

## Qué exige el trabajo final

- Diseñar al menos 15 casos sobre tres componentes reales, con válidos, inválidos, límites y excepciones.
- Identificar ID, requisito/riesgo, unidad, preparación, esperado y ejecución; aislar dependencias cuando corresponda.
- Conservar reporte y regresión después de un cambio de G02.
- Medir al menos 80 % de líneas del paquete de negocio con inclusiones/exclusiones declaradas.
- Introducir un defecto temporal, comprobar fallo real y restaurar y repetir, sin capturar AssertionError para ocultarlo.

## Procedimiento realizado

Se ejecutaron pruebas de negocio con Spring/H2, HTTP con MockMvc, concurrencia y PostgreSQL temporal. DF-01 añadió la regresión de recarga del color. Se alteró temporalmente el máximo de reprogramaciones, se observó una aserción fallida y se restauró la regla de dos.

## Resultados obtenidos

Los XML publicados de DF-01 registran 44 casos aprobados: 31 BusinessTests, 11 WebTests, 1 ConcurrencyTests y 1 PostgresTests. La cobertura histórica anterior, de 43 casos, es 310/329 líneas = 94,22 % del paquete service; ramas 111/165 = 67,27 %, según el informe. No es una ejecución nueva realizada al crear esta Wiki.

## Análisis

Las pruebas de negocio son de componentes con integración Spring/H2, no unidades totalmente aisladas. Cobertura de líneas no prueba todos los caminos. La cobertura histórica y la regresión posterior tienen distinto corte; los cambios posteriores documentados no modificaron las clases del paquete medido.

## Conclusiones

La suite y el defecto controlado aportan evidencia de reglas y regresión. G07 conserva pendiente consolidar catálogo, relación con riesgos y ejecución sobre la versión final.

## Ubicación de evidencias

- [Fuentes JUnit](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/tree/main/src/test/java/pe/unsa/vetagenda)
- [XML de 44 casos](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/tree/main/docs/evidencias/DF-01)
- [Cobertura histórica HTML/XML/CSV](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/tree/main/docs/evidencias/cobertura)
- [Fallo controlado](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/evidencias/defecto-controlado.log)
- [Regresión tras restauración](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/evidencias/regresion-final.log)

## Reproducción

```sh
mvn clean verify
mvn -Dtest=BusinessTests#enforcesTwoReschedules test
```

El segundo comando solo debe fallar después de introducir el defecto en una copia de trabajo. Restaurar el fuente y ejecutar de nuevo la suite. Los comandos documentan reproducción; no se ejecutaron al redactar esta Wiki.

## Pendientes y limitaciones

- [ ] Consolidar catálogo con ID, requisito/riesgo, datos, esperado, fecha, versión y XML.
- [ ] Revisar necesidad de dobles y declarar límites del aislamiento.
- [ ] Repetir sobre versión final si cambia código y conservar el reporte y su identidad.

## Criterio de cierre

El docente ejecuta la suite y reproduce el fallo controlado y su restauración; la cobertura tiene un alcance declarado.

## Cuestionarios y referencias

Los cuestionarios específicos de PRAC01–PRAC09 deben contrastarse con cada guía original. Esta página organiza la aplicación al producto desde la consigna integradora; no declara resueltos cuestionarios cuyo texto no se ha verificado. Véase [Fuentes y criterios](Fuentes-y-criterios).

[Inicio](Home) · [Evidencias y entregables](Evidencias-y-entregables) · [Trazabilidad y resultados](Trazabilidad-y-resultados)
