# ERS de trabajo — VetAgenda 0.1.0

Fuente: Entregable 1, tablas 11 y 12, y aclaraciones reales de esta conversación. Es una línea base técnica de trabajo; faltan la elicitación y validación formal de G01. No se atribuyen entrevistas ni firmas. Responsable del registro: DelCastilloBrad.

Los criterios siguientes concretan las reglas implementadas del README. La prioridad procede del primer entregable. La evidencia y el estado detallados se mantienen en Issues y en TRAZABILIDAD.md.

| ID | Requisito | Prioridad | Actor | Criterio de aceptación |
|---|---|---|---|---|
| RF-01 | Autenticación y cierre de sesión | Alta | Todos | Iniciar con correo y contraseña válidos; rechazar credenciales inválidas; bloquear 15 min tras 5 fallos; cerrar sesión y proteger rutas. |
| RF-02 | Autorregistro de clientes | Alta | Cliente | Validar consentimiento, DNI de 8 dígitos, correo único, teléfono y contraseña de 10 a 72 bytes; almacenar hash BCrypt. |
| RF-03 | Registro, edición y lista de mascotas | Alta | Cliente, Recepción | Guardar y listar mascotas asociadas al propietario; impedir gestionar mascotas ajenas y fechas de nacimiento futuras; verificar además la edición existente. |
| RF-04 | Consulta de disponibilidad | Alta | Cliente, Recepción | Ofrecer bloques válidos de 30 min de lunes a sábado 08–19 h, respetar cierres/feriados y límites inclusivos de 2 h y 30 días en Lima. |
| RF-05 | Reserva sin superposición | Alta | Cliente, Recepción | Reservar un bloque válido; impedir citas activas simultáneas del mismo veterinario o mascota; una sola reserva gana ante concurrencia. Aplicar bloqueo de inasistencias al cliente. |
| RF-06 | Reprogramación y cancelación | Alta | Cliente, Recepción | Cliente cambia o cancela con al menos 2 h; recepción puede gestionar fuera del plazo original; máximo 2 reprogramaciones, solo RESERVADA; nuevo bloque válido y liberación del anterior. |
| RF-07 | Confirmación de llegada | Alta | Recepción | Cambiar RESERVADA a EN_ESPERA solo por recepción y el día de la cita. |
| RF-08 | Registro de atención clínica | Alta | Veterinario | Solo el veterinario asignado atiende una cita EN_ESPERA; validar peso positivo y ficha; cerrar ATENDIDA y conservar historial. |
| RF-09 | Inasistencias y restricciones | Media | Recepción | Permitir NO_ASISTIO desde +15 min; 3 faltas en 90 días bloquean reserva en línea; recepción mantiene reserva asistida. |
| RF-10 | Agenda e historial por mascota | Alta | Veterinario, Recepción, Cliente propio | Consultar citas según rol y conservar atención en historial de la mascota; cliente solo consulta sus datos. |
| RF-11 | Reporte consolidado de citas | Alta | Administrador | Filtrar por fechas, estado, servicio y veterinario; mostrar total, distribución y tasa NO_ASISTIO/total; contrastar combinaciones y total cero. |
| RF-12 | Administración de catálogos y usuarios | Media | Administrador | Gestionar usuarios internos, veterinarios, servicios, horarios y feriados; impedir desactivar al propio administrador; revisar manualmente altas y cambios de todos los catálogos. |

| ID | Atributo | Umbral de aceptación | Evidencia actual |
|---|---|---|---|
| RNF-01 | Rendimiento | p95 ≤ 2 s y error ≤ 1 % con 10 usuarios durante 5 min en disponibilidad y reserva. | JMeter pendiente |
| RNF-02 | Seguridad | 100 % de contraseñas con BCrypt; rutas no autorizadas redirigen al login o deniegan acceso. | Controles probados; inventario exhaustivo de rutas pendiente |
| RNF-03 | Usabilidad | ≥80 % de usuarios nuevos reservan en ≤2 min y ≤5 pasos. | Sesión con usuarios pendiente |
| RNF-04 | Persistencia y recuperación | 0 registros perdidos tras 3 reinicios; restauración ≤10 min. | Scripts entregados; medición pendiente |
| RNF-05 | Mantenibilidad | ≥80 % de líneas cubiertas en paquete service. | Verificado: 310/329 = 94,22 %, reporte inicial; servicios sin cambios posteriores |
| RNF-06 | Portabilidad | Funcionamiento Chrome y Firefox; ancho 360 px sin desbordamiento horizontal. | Chrome y 360 px aprobados; Firefox pendiente |

## Cambios a la línea base

CC-03 añade personalización de estilos (aceptada e implementada). CC-04 propone ampliar operaciones de recepción al administrador y queda diferida por decisión del usuario; ADMIN conserva sus permisos actuales. No se amplía atención clínica al administrador. Los candidatos preliminares CC-01 (limpieza) y CC-02 (lista de espera) no fueron solicitudes efectivamente aprobadas ni están implementados.
