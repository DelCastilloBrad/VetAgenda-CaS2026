# Verificación del proyecto PILAR - VetAgenda

Fecha: **25/11/2026 (America/Lima)**. Herramienta: **PILAR RM 2026.1.1**.

## Informe

- [Abrir el informe PDF](Informe_Verificacion_PILAR_VetAgenda.pdf)
- [Capturas originales](capturas/)
- [Procedencia y hashes SHA-256](manifiesto.json)

## Resultado

Se verificaron visualmente la apertura de `VetAgenda.mgr`, los cinco activos, sus veinte valoraciones propias, las siete dependencias, los ocho escenarios priorizados y las ramas IA.3/IA.4 en dominio VA (`current` L2, `target` L3). Las tres fases muestran resultados calculados.

| Fase | D | I | C | A | T |
|---|---:|---:|---:|---:|---:|
| potencial | 5,3 | 5,7 | 7,5 | 5,9 | 4,5 |
| current | 5,3 | 4,9 | 6,8 | 5,8 | 4,4 |
| target | 5,3 | 4,9 | 6,6 | 5,8 | 4,4 |

Son valores cualitativos del modelo PILAR, no porcentajes ni mediciones empíricas. El objetivo L3 y la fase target representan mejoras propuestas.

## Verificación técnica del paquete recibido

- ZIP sin fallos CRC detectados.
- Tres XML válidos contra los esquemas incluidos.
- Dos scripts ejecutados sin errores; regeneración idéntica de las entradas.
- Consistencia entre CSV, JSON y XML para los ocho escenarios.
- SHA-256 del `.mgr` revisado: `eae4b359a56d2be042c442f3d35ee77d3b4b09a25d49955b1eb5b0adcbc6fb0d`.

## Alcance y pendientes

La verificación acredita el funcionamiento del archivo y la configuración observada. No cierra G06: quedan por revisar las amenazas adicionales del catálogo, completar el plan de tratamiento y vincular pruebas de eficacia a cada medida. No se han ejecutado nuevas pruebas de la aplicación durante esta documentación.

Las capturas proceden del usuario. Se publican los PNG originales renombrados; los recortes de presentación del PDF no alteran los originales. El `.mgr` y el ZIP auxiliar no se incluyen en esta carpeta. Los CSV del paquete son matrices auxiliares y las capturas no son exportaciones nativas de PILAR.

## Índice de evidencias

| Figura | Evidencia |
|---|---|
| 1 | [Árbol del proyecto y módulos disponibles](capturas/01-arbol-proyecto.png) |
| 2 | [Identificación de AS01](capturas/02-activo-as01.png) |
| 3 | [Identificación de AS05](capturas/03-activo-as05.png) |
| 4 | [Valoración propia de los activos](capturas/04-valoracion-activos.png) |
| 5 | [Siete dependencias desplegadas](capturas/05-dependencias.png) |
| 6 | [Frecuencias y degradaciones de amenazas](capturas/06-amenazas.png) |
| 7 | [IA.3 e IA.4 en el dominio VA](capturas/07-salvaguardas-va.png) |
| 8 | [Riesgo potencial](capturas/08-riesgo-potencial.png) |
| 9 | [Riesgo current](capturas/09-riesgo-current.png) |
| 10 | [Riesgo target](capturas/10-riesgo-target.png) |

[Volver a G06](../../../wiki/G06-Gestion-de-riesgos.md)
