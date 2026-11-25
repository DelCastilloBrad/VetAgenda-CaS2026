# G09 · Gestión de calidad con Selenium

**Producto:** VetAgenda 0.1.0 · **Estado:** avance con pendientes de cierre · [Seguimiento G09](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues/31)

## Objetivo

Verificar los tres procesos mediante flujos web y emitir un dictamen sustentado en requisitos, riesgos, pruebas y rendimiento.

## Herramienta, versión y requisitos vinculados

Selenium 4.54.0 y pytest 9.0.1 fijados en requirements.txt; Chrome 158.0.8127.88 reportado por el usuario. Selenium Manager o controlador local compatible.

Acceso, credenciales inválidas, roles, registro, datos inválidos, reserva, llegada/atención, historial, logout, tema, ancho 360 px y reprogramación/cancelación.

## Qué exige el trabajo final

- Automatizar al menos ocho casos web y cubrir los tres procesos del producto.
- Usar aserciones observables, selectores estables, esperas explícitas y datos aislados.
- Conservar suite, reportes, capturas de fallos y defectos.
- Ejecutar regresión final e integrar evidencia en dictamen apto, apto con observaciones o no apto.

## Procedimiento realizado

Se corrigieron DF-01 (valor del selector de color tras recarga) y DF-02 (clic interceptado en la automatización). La regresión conservó aserciones y 11 casos. La suite requiere una base demo desechable porque modifica la cita inicial y crea datos ficticios.

## Resultados obtenidos

La captura publicada registra 11 casos aprobados en 28,55 s. El informe posterior documenta XML final de 28,557 s, 0 fallos/errores/omitidos, del 24/11/2026 20:27:19 Lima, todavía pendiente de incorporación aquí. Firefox no se ejecutó.

## Análisis

La aprobación de Chrome y 360 px no acredita Firefox ni usabilidad con usuarios. El XML de intento bloqueado es un antecedente fallido, no un reporte de aprobación. El dictamen integra los límites de recuperación, elicitación, diseño, estimación y rendimiento.

## Conclusiones

Dictamen del avance: apto con observaciones para demostración local de v0.1.0. No implica liberación real ni cierre de las nueve guías; requiere regresión y evidencias coherentes con la versión final.

## Ubicación de evidencias

- [Suite de 11 casos](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/tests/selenium/test_flows.py)
- [Dependencias fijadas](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/tests/selenium/requirements.txt)
- [Captura de la regresión](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/evidencias/selenium-consola-usuario.png)
- [DF-01 y DF-02](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/blob/main/docs/DEFECTOS.md)
- [Resultados y dictamen](Trazabilidad-y-resultados)

## Reproducción

```sh
python -m pip install -r tests/selenium/requirements.txt
python -m pytest tests/selenium -q --junitxml=selenium-resultados.xml
```

Activar antes un entorno Python e iniciar la aplicación en una base demo desechable. BASE_URL permite indicar el puerto. No ejecutar la suite sobre datos de trabajo.

## Pendientes y limitaciones

- [ ] Incorporar XML final y conservar fecha, versión y hash sin atribuir fuente/ejecutable no demostrado.
- [ ] Comprobar Firefox para RNF-06 y realizar las otras mediciones pendientes.
- [ ] Repetir suite sobre la versión etiquetada final y actualizar dictamen integrado.

## Criterio de cierre

Los casos son repetibles y el dictamen refleja resultados observables y limitaciones.

## Cuestionarios y referencias

Los cuestionarios específicos de PRAC01–PRAC09 deben contrastarse con cada guía original. Esta página organiza la aplicación al producto desde la consigna integradora; no declara resueltos cuestionarios cuyo texto no se ha verificado. Véase [Fuentes y criterios](Fuentes-y-criterios).

[Inicio](Home) · [Evidencias y entregables](Evidencias-y-entregables) · [Trazabilidad y resultados](Trazabilidad-y-resultados)
