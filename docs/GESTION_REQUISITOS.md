# Gestión de requisitos y cambios — G02

Se utiliza GitHub Issues como gestor equivalente de incidencias, permitido por el enunciado. El repositorio conserva código, ERS y evidencias, y cada issue tiene responsable, criterio de aceptación y enlaces. GitHub no reemplaza el repositorio SVN solicitado en G05.

## Flujo operativo

| Estado del proceso | Registro en GitHub | Condición de salida |
|---|---|---|
| Registrado | Issue abierto con ID, fuente, responsable y criterio | Entrada completa |
| Analizado | Comentario con alcance, impacto y dependencias | Criterios revisables |
| Aprobado / Rechazado | Comentario que identifica decisión y origen | No atribuir aprobación a un cliente ficticio |
| Implementado | Comentario enlaza versión y archivos | Cambio disponible para revisar |
| Verificado | Comentario enlaza caso y evidencia con resultado | Criterio comprobado o limitaciones explícitas |
| Cerrado | Issue cerrado como completado o no planificado | Cierre motivado, reabrible si aparece regresión |

Los rechazos pasan de decisión a cierre sin inventar implementación o prueba. Una solicitud diferida permanece abierta y no se implementa hasta retomarse. Los estados intermedios se conservan en comentarios; GitHub aporta fecha UTC y autor. No se ha creado un tablero Projects ni se depende de etiquetas personalizadas.

## Importación honesta de trabajo previo

El seguimiento en Issues comienza con esta publicación. El código y las pruebas existían antes del registro. Los comentarios describen la revisión e importación de esa línea base, con las fechas reales del gestor; no reconstruyen fechas de entrevistas, aprobaciones o desarrollo. Los cinco requisitos de seguimiento prioritario son RF-01, RF-04, RF-05, RF-06 y RF-08, como proponía el primer entregable. La autorización del usuario permite publicar y organizar esta línea base; no sustituye la aceptación formal del representante de la clínica.

Responsable de registro y revisión técnica: DelCastilloBrad (cuenta del repositorio). Los demás integrantes no se asignan sin conocer sus cuentas ni se simulan revisiones independientes. La evidencia Selenium final disponible es la captura del usuario; falta incorporar su XML.

## Comparación de herramientas

| Criterio para este trabajo | GitHub Issues (elegida) | REM / gestor especializado de requisitos |
|---|---|---|
| Código y pruebas | Enlaces directos a archivos y commits | Requiere enlazar o importar artefactos externos |
| Historial verificable | Issues, comentarios y eventos con autor y fecha | Historial según configuración de la herramienta |
| Flujo y atributos | Convención documentada y plantillas; campos limitados sin Projects | Modelado especializado de requisitos, según producto/versión |
| Exportación entregada | CSV y JSON de este registro y comentarios | Depende del formato y versión instalados |
| Adecuación | Gestor equivalente de incidencias admitido en la consigna | Alternativa citada expresamente por la consigna |

Esta es una comparación de adecuación documental, no una evaluación experimental de REM instalado. La elección aprovecha el repositorio existente y mantiene visibles los pendientes. G02 sigue abierto hasta revisar el registro y completar el informe con sus evidencias; la fecha de importación no demuestra que el gestor se usara durante todo el desarrollo.

## Trazabilidad y exportación

SEGUIMIENTO.md enlaza cada issue. `seguimiento/issues.json` conserva las respuestas del gestor y los comentarios de transición; `seguimiento/issues.csv` es el resumen tabular. Son instantáneas con fecha de exportación; el estado vivo está en Issues. No se reutilizan los números de cambio preliminares para solicitudes distintas.
