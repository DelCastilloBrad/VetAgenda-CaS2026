# G06 · Gestión de riesgos

**Producto:** VetAgenda 0.1.0 · **Estado:** avance con pendientes de cierre · [Seguimiento G06](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/28)

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

El informe registra un proyecto PILAR, inventario y amenazas priorizadas, y valoración de ramas de autenticación. La aplicación incluye permisos, propiedad de datos, CSRF, BCrypt, bloqueo temporal y revocación de cuentas desactivadas; hay pruebas sobre esos controles.

## Resultados obtenidos

El archivo VetAgenda.mgr y el paquete VetAgenda_PILAR.zip se conservan fuera de este repositorio. El informe advierte que frecuencia, degradación y madurez son supuestos académicos y que los menús de exportación empleados estaban deshabilitados en evaluación.

## Análisis

Una matriz simplificada no equivale a aplicar MAGERIT completo. La propagación de madurez a subcontroles no acredita controles auditados uno a uno. La reducción prevista de riesgo no es una reducción empírica comprobada. Falta revisar catálogo y plan de tratamiento completo.

## Conclusiones

Hay modelado de riesgo reportado y controles de aplicación probados. G06 no queda cerrado sin originales, responsables y evidencias de cada tratamiento priorizado.

## Ubicación de evidencias

- [Controles de seguridad](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/src/main/java/pe/unsa/vetagenda/config/SecurityConfig.java)
- [Revocación de cuentas](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/src/main/java/pe/unsa/vetagenda/config/ActiveAccountFilter.java)
- [Pruebas HTTP de controles](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/src/test/java/pe/unsa/vetagenda/WebTests.java)
- [Evidencias externas y criterios](Evidencias-y-entregables)

## Pendientes y limitaciones

- [ ] Incorporar proyecto nativo, inventario y escenarios con dependencias y escalas documentadas.
- [ ] Completar responsables, fechas, costo/esfuerzo, indicadores y evidencia de dos tratamientos.
- [ ] Revisar amenazas heredadas y distinguir supuestos, objetivos y valores actuales; aclarar RMAT con el docente.

## Criterio de cierre

Cada riesgo prioritario tiene responsable y respuesta; dos tratamientos implantados tienen evidencia de funcionamiento.

## Cuestionarios y referencias

Los cuestionarios específicos de PRAC01–PRAC09 deben contrastarse con cada guía original. Esta página organiza la aplicación al producto desde la consigna integradora; no declara resueltos cuestionarios cuyo texto no se ha verificado. Véase [Fuentes y criterios](Fuentes-y-criterios).

[Inicio](Home) · [Evidencias y entregables](Evidencias-y-entregables) · [Trazabilidad y resultados](Trazabilidad-y-resultados)
