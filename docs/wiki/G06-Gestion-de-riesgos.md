# G06 · Gestión de riesgos

**Producto:** VetAgenda 0.1.0 · **Estado:** funcionamiento de PILAR verificado; cierre integral pendiente · [Seguimiento G06](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/28)

## Objetivo

Analizar activos, amenazas y salvaguardas del sistema que soporta VetAgenda y verificar tratamientos del producto.

## Herramienta, versión y requisitos vinculados

MAGERIT y PILAR RM 2026.1.1 (28/05/2026), biblioteca INFOSEC std_20251.pl5 y licencia de evaluación, según el informe. Las matrices auxiliares no son exportaciones nativas de PILAR.

Datos de cuentas y atención, aplicación, base de datos y entorno operativo; RNF-02 y RNF-04, con controles de acceso y conservación de datos.

## Qué exige el trabajo final

- Identificar al menos cinco activos con dependencias y dimensiones de seguridad.
- Documentar al menos ocho escenarios activo–amenaza, escalas, frecuencia, degradación, impacto y riesgo.
- Priorizar riesgos y asignar salvaguardas, responsable, fecha, costo/esfuerzo, indicador y residual previsto.
- Implementar y verificar al menos dos tratamientos; diferenciar propuestos e implantados.
- Entregar proyecto/exportación y comparar PILAR con RMAT solo si ese recurso está identificado inequívocamente.

## Procedimiento realizado

El 25/11/2026 el usuario abrió el proyecto en PILAR RM y aportó diez capturas de identificación, valoración, dependencias, amenazas, salvaguardas y resultados. Se contrastaron con los XML, CSV y JSON del paquete. La comprobación técnica del ZIP no detectó errores CRC; los tres XML validan contra sus esquemas y ambos scripts regeneran las mismas entradas sin errores. El informe de verificación reúne el procedimiento y sus límites. La aplicación incluye permisos, propiedad de datos, CSRF, BCrypt, bloqueo temporal y revocación de cuentas desactivadas; hay pruebas sobre esos controles.

## Resultados obtenidos

El [informe PDF de verificación](../evidencias/PILAR/2026-11-25/Informe_Verificacion_PILAR_VetAgenda.pdf), las [diez capturas originales](../evidencias/PILAR/2026-11-25/capturas/) y el [manifiesto SHA-256](../evidencias/PILAR/2026-11-25/manifiesto.json) se publican en este repositorio.

| Elemento | Resultado comprobado |
|---|---|
| Inventario y valoración | 5 activos, 20 valores propios D/I/C/A/T en AS01-AS04; AS05 recibe valor mediante dependencias. |
| Dependencias | 7 relaciones: AS01 depende de AS02, AS03, AS04 y AS05; AS02-AS04 dependen de AS05. |
| Amenazas priorizadas | 8 pares activo-amenaza con frecuencias y degradaciones coincidentes con las entradas. |
| Salvaguardas | IA.3 e IA.4, dominio VA: current L2 y target L3. |
| Cálculo | Resultados visibles en potencial, current y target. |

| Fase | D | I | C | A | T |
|---|---:|---:|---:|---:|---:|
| potencial | 5,3 | 5,7 | 7,5 | 5,9 | 4,5 |
| current | 5,3 | 4,9 | 6,8 | 5,8 | 4,4 |
| target | 5,3 | 4,9 | 6,6 | 5,8 | 4,4 |

Son valores cualitativos del modelo, no porcentajes ni mediciones empíricas. L3 es un objetivo propuesto. Las frecuencias, degradaciones y madurez son supuestos académicos; los menús de exportación documentados estaban deshabilitados en la licencia de evaluación. Esta revisión no ejecuta nuevas pruebas de la aplicación ni acredita por sí sola la eficacia completa de cada tratamiento.

El archivo VetAgenda.mgr y el paquete VetAgenda_PILAR.zip aún se conservan fuera del repositorio. SHA-256 del archivo nativo revisado: `eae4b359a56d2be042c442f3d35ee77d3b4b09a25d49955b1eb5b0adcbc6fb0d`.

## Análisis

Una matriz simplificada no equivale a aplicar MAGERIT completo. La propagación de madurez a subcontroles no acredita controles auditados uno a uno. La reducción prevista de riesgo no es una reducción empírica comprobada. El cálculo también incluye amenazas adicionales a las ocho priorizadas, entre ellas AS02/A.11, AS04/A.5 y AS01/A.24. Falta revisar catálogo y plan de tratamiento completo.

## Conclusiones

Se verificó la apertura del archivo y la configuración de activos, dependencias, ocho escenarios y dos ramas de salvaguardas. Las tres fases calculan resultados coincidentes con los valores documentados. G06 no queda cerrado sin originales, responsables y evidencias de eficacia de cada tratamiento priorizado.

## Ubicación de evidencias

- [Informe PDF con capturas](../evidencias/PILAR/2026-11-25/Informe_Verificacion_PILAR_VetAgenda.pdf)
- [Carpeta de verificación, capturas y manifiesto](../evidencias/PILAR/2026-11-25/)

- [Controles de seguridad](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/src/main/java/pe/unsa/vetagenda/config/SecurityConfig.java)
- [Revocación de cuentas](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/src/main/java/pe/unsa/vetagenda/config/ActiveAccountFilter.java)
- [Pruebas HTTP de controles](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/src/test/java/pe/unsa/vetagenda/WebTests.java)
- [Evidencias externas y criterios](Evidencias-y-entregables)

## Pendientes y limitaciones

- [x] Incorporar informe visual de apertura, inventario, dependencias, amenazas, salvaguardas y resultados.
- [ ] Incorporar proyecto nativo y paquete de entradas, inventario y escenarios editables.
- [ ] Completar responsables, fechas, costo/esfuerzo, indicadores y evidencia de dos tratamientos.
- [ ] Revisar amenazas adicionales y justificar supuestos y controles actuales; aclarar RMAT con el docente.
- [ ] Ensayar restauración de la base de datos y completar medidas de continuidad.

## Criterio de cierre

Cada riesgo prioritario tiene responsable y respuesta; dos tratamientos implantados tienen evidencia de funcionamiento. La publicación del informe no cambia por sí sola estados de Issues ni acredita la implantación de target.

## Cuestionarios y referencias

Los cuestionarios específicos de PRAC01–PRAC09 deben contrastarse con cada guía original. Esta página organiza la aplicación al producto desde la consigna integradora; no declara resueltos cuestionarios cuyo texto no se ha verificado. Véase [Fuentes y criterios](Fuentes-y-criterios).

[Inicio](Home) · [Evidencias y entregables](Evidencias-y-entregables) · [Trazabilidad y resultados](Trazabilidad-y-resultados)

