# G04 · Dimensionamiento y estimación

**Producto:** VetAgenda 0.1.0 · **Estado:** avance con pendientes de cierre · [Seguimiento G04](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/26)

## Objetivo

Estimar tamaño funcional, esfuerzo, duración y costo del mismo alcance de VetAgenda de forma recalculable.

## Herramienta, versión y requisitos vinculados

Excel o LibreOffice Calc para un libro editable con fórmulas; métodos NESMA, COSMIC y COCOMO. No consta todavía un libro de cálculo final.

La frontera del monolito VetAgenda y la línea base de 12 RF y 6 RNF. Los métodos deben medir un alcance equivalente.

## Qué exige el trabajo final

- Declarar frontera e hipótesis; contar NESMA con tipo de conteo, componentes, reglas y cálculo.
- Medir COSMIC por procesos funcionales y movimientos de entrada, salida, lectura y escritura; conservar sus unidades separadas.
- Declarar variante COCOMO, tamaño, parámetros y fuentes; justificar cualquier conversión a líneas de código.
- Calcular costo en soles y plazo para una y tres personas con dedicación, horas/mes, tarifa y coordinación.
- Comparar métodos y horas reales registradas y evaluar sensibilidad de un parámetro en ±20 %.

## Procedimiento realizado

Se delimitó funcionalmente el producto en la ERS y en la descripción de arquitectura. Esa información es el insumo para los conteos; no se han convertido requisitos directamente en puntos de función.

## Resultados obtenidos

Alcance y componentes disponibles. La evidencia revisada no acredita un libro NESMA/COSMIC/COCOMO completo, costos ni sensibilidad; estos resultados siguen pendientes.

## Análisis

Tamaño, esfuerzo y duración son magnitudes distintas. No corresponde dividir automáticamente el plazo entre tres ni presentar horas reconstruidas como horas reales. La mención «wine» de la consigna no define un método y requiere aclaración docente.

## Conclusiones

G04 dispone de entradas documentadas, pero su estimación queda pendiente de cálculo editable y validación.

## Ubicación de evidencias

- [Línea base funcional](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/ERS.md)
- [Frontera y componentes](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/ARQUITECTURA.md)

## Pendientes y limitaciones

- [ ] Preparar libro editable con conteos, parámetros, fórmulas y memoria de cálculo.
- [ ] Comparar escenarios de una y tres personas y sensibilidad ±20 %.
- [ ] Registrar esfuerzo real desde ahora y declarar limitaciones si no existen horas históricas.

## Criterio de cierre

El evaluador puede recalcular valores y distinguir tamaño, esfuerzo, duración y costo sin fórmulas ocultas.

## Cuestionarios y referencias

Los cuestionarios específicos de PRAC01–PRAC09 deben contrastarse con cada guía original. Esta página organiza la aplicación al producto desde la consigna integradora; no declara resueltos cuestionarios cuyo texto no se ha verificado. Véase [Fuentes y criterios](Fuentes-y-criterios).

[Inicio](Home) · [Evidencias y entregables](Evidencias-y-entregables) · [Trazabilidad y resultados](Trazabilidad-y-resultados)
