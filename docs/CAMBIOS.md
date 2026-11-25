# Solicitudes reales de cambio

## CC-03 — Estilos reemplazables y personalizables

**Fuente:** solicitud del usuario: «haz que los estilos puedan ser reemplazables […] personalizar […] modificar ciertas cosas sin romper». Se registra ahora una decisión previa real; no se inventa su hora original. Prioridad media. Decisión: aceptada e implementada en v0.1.0; verificación disponible con observación documental (XML Selenium final pendiente).

- Motivo: adaptar identidad visual sin alterar comportamiento.
- ERS/diseño: añade administración de tema y separa tokens CSS, archivo externo y selectores funcionales. Véanse PERSONALIZACION.md y ARQUITECTURA.md.
- Código: ThemeService, ThemeController, appearance.html y CSS. Parámetros permitidos validados, persistencia en base y restauración de valores.
- Costo/plazo: afecta capa visual, persistencia de configuración y regresión. No hay horas ni costo medidos; no se presenta una estimación retroactiva como dato real.
- Riesgos: valores inválidos, bajo contraste elegido por el usuario y regresión de formulario; se valida formato y se permite restablecer. No se declara auditoría de accesibilidad.
- Pruebas: BusinessTests.themeValidatesValuesAndRoles; WebTests.appearanceShowsStoredColorAfterReload; Selenium 09. DF-01 se corrigió antes de la regresión final de 11 casos aprobados.
- Estado: verificado y cerrado técnicamente; la publicación enlaza implementación y evidencia. No implica aceptación formal de una clínica.

## CC-04 — Permitir operaciones de recepción al administrador

**Fuente:** conversación sobre por qué el administrador no puede crear mascotas y realizar operaciones de recepción. El usuario decidió: «por ahora para estas pruebas lo dejaremos así […] para la prueba en vivo ya lo modificamos». Prioridad media. Decisión: diferida al siguiente incremento; no implementada en v0.1.0.

- Motivo: facilitar operación en una clínica pequeña cuando el administrador también atiende recepción.
- Alcance propuesto: crear/editar mascotas y reservar/reprogramar/cancelar, llegada e inasistencias. La atención clínica sigue reservada al veterinario asignado.
- Impacto: ERS RF-03 a RF-07 y RF-09; matriz de roles, rutas Spring Security, comprobaciones en servicios, menú y formularios. Evitar cambiar solo la visibilidad del menú.
- Costo/plazo: requiere revisar todas las capas de autorización y repetir pruebas; no se dispone de una medición o estimación aprobada. Se programa fuera de las pruebas actuales por decisión expresa del usuario.
- Riesgos: concesión accidental de acceso clínico o datos ajenos; probar roles positivos y negativos y autoría de operaciones.
- Pruebas de aceptación pendientes: ADMIN completa operaciones de recepción; CLIENTE mantiene propiedad y restricciones; ningún rol no veterinario registra atención; repetir JUnit y Selenium completos.
- Estado: analizada y diferida, issue abierto. No se modifica el código en esta publicación.

## Candidatos del primer entregable

CC-01 (10 min de limpieza entre cirugías) y CC-02 (lista de espera) eran candidatos de planificación. No constan como cambios efectivamente solicitados/aprobados. Se preservan como antecedentes, sin fingir implementación. El documento local anterior llamó CC-01 a estilos: se normaliza a CC-03 para evitar colisión con la propuesta inicial.
