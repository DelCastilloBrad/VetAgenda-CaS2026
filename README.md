# VetAgenda

Aplicación web local para una clínica veterinaria: reservas, llegada, atención, historial, reprogramaciones, cancelaciones, inasistencias y reportes. Java 21, Spring Boot 3.4.13, Maven, Thymeleaf, Bootstrap local, Spring Security, JDBC y Flyway. PostgreSQL es la base del perfil principal; el perfil de demostración usa H2 persistente para facilitar la primera ejecución.

## Abrir la demostración

Necesitas **JDK 21 y Maven 3.9 o posterior**. Desde un clon del repositorio:

```sh
mvn clean package
java -jar target/vetagenda-0.1.0.jar --spring.profiles.active=demo
```

Abre **http://localhost:8080**. También puedes ejecutar `scripts/demo.bat` en Windows o `sh scripts/demo.sh` en Linux/macOS. Para detener: Ctrl+C en la terminal.

| Rol | Correo | Contraseña de demostración |
|---|---|---|
| Administrador | admin@vetagenda.test | DemoVet2026! |
| Recepción | recepcion@vetagenda.test | DemoVet2026! |
| Veterinario | vet@vetagenda.test | DemoVet2026! |
| Cliente | cliente@vetagenda.test | DemoVet2026! |

Los datos de estas cuentas son ficticios. El perfil demo crea usuarios y mascotas solo si la base está vacía. Incluye una cita reservada para el día del primer arranque, para probar la llegada y la atención. Los datos se guardan en `data/` y se conservan al reiniciar. Ejecuta desde la misma carpeta para usar esa misma base. No se incluyen los datos generados durante nuestras pruebas.

## Personalizar el diseño

Entra como administrador y abre **Apariencia**. Cambia colores, tipografía, escala y bordes; guarda o restablece el diseño. Para reemplazar el tema por un archivo editable, copia `config/theme/custom.example.css` a `config/theme/custom.css`.

La guía completa está en [docs/PERSONALIZACION.md](docs/PERSONALIZACION.md). Las variables visuales están separadas de la lógica y de los selectores que utilizan las pruebas.

## Usar PostgreSQL

Instala PostgreSQL o levanta la base con Docker Compose. Para esta última opción, copia `.env.example` a `.env`, define una contraseña local y ejecuta `docker compose up -d database`. Compose solo inicia la base; Java se ejecuta en el equipo anfitrión.

Si instalas PostgreSQL directamente, crea el usuario `vetagenda` y una base `vetagenda` de su propiedad. El usuario debe poder crear el esquema mediante Flyway.

Linux/macOS:

```sh
export DB_URL='jdbc:postgresql://localhost:5432/vetagenda'
export DB_USER='vetagenda'
export DB_PASSWORD='tu-clave-local'
export BOOTSTRAP_EMAIL='admin@vetagenda.local'
export BOOTSTRAP_PASSWORD='elige-una-clave-de-al-menos-10-caracteres'
java -jar target/vetagenda-0.1.0.jar
```

PowerShell:

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/vetagenda'
$env:DB_USER = 'vetagenda'
$env:DB_PASSWORD = 'tu-clave-local'
$env:BOOTSTRAP_EMAIL = 'admin@vetagenda.local'
$env:BOOTSTRAP_PASSWORD = 'elige-una-clave-de-al-menos-10-caracteres'
java -jar target/vetagenda-0.1.0.jar
```

`BOOTSTRAP_PASSWORD` crea el primer administrador únicamente cuando todavía no existe uno. Retira esa variable en arranques posteriores. Las cuentas demo no se crean en el perfil principal. Los clientes se autorregistran; el administrador agrega usuarios internos. La contraseña admite de 10 a 72 bytes. El correo del administrador inicial debe tener formato de correo válido.

Flyway crea automáticamente las tablas desde `src/main/resources/db/migration/`. No edites una migración que ya se haya aplicado: agrega una nueva versión.

## Compilar y probar

Instala **JDK 21 y Maven 3.9 o posterior**. La primera compilación descarga las dependencias de Maven Central.

```sh
mvn clean verify
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

El JAR recién compilado queda en `target/vetagenda-0.1.0.jar`. Para actualizar el acceso rápido de demostración, reemplaza `dist/vetagenda.jar` por ese JAR.

La suite JUnit incluye reglas de negocio, permisos HTTP, bloqueo de acceso, concurrencia y una prueba con PostgreSQL real temporal. Esta última usa `embedded-postgres`: requiere un sistema compatible y ejecución como usuario normal. No necesita una instancia PostgreSQL instalada para la prueba. Los reportes se generan en `target/surefire-reports/` y la cobertura en `target/site/jacoco/index.html`.

### Selenium

Con la aplicación en una base demo desechable recién creada, desde la raíz del proyecto:

```sh
python -m venv .venv
# Activa .venv según tu sistema.
python -m pip install -r tests/selenium/requirements.txt
python -m pytest tests/selenium -q --junitxml=selenium-resultados.xml
```

Requiere Chrome/Chromium. Selenium Manager resuelve el controlador; puedes indicar `CHROMEDRIVER` y `CHROME_BINARY` si ya los tienes. `BASE_URL` cambia la dirección. Los 11 casos crean datos ficticios con identificadores únicos. El caso de atención modifica la cita inicial 1: repite la suite sobre una base nueva de pruebas, no sobre datos reales.

**Regresión ejecutada por el usuario: 11 casos Selenium aprobados en 28,55 s con Chrome 158.0.8127.88**, incluida la vista de 360 px. La captura de consola se conserva en [docs/evidencias/selenium-consola-usuario.png](docs/evidencias/selenium-consola-usuario.png). El XML final está pendiente de incorporar; Firefox y JMeter siguen pendientes. El intento bloqueado anterior se conserva como antecedente.

## Reglas implementadas

- Lunes a sábado, 08:00–19:00; bloques de 30 minutos. Administración puede editar días y horarios y registrar feriados. Las citas existentes se conservan cuando se modifica la configuración.
- Anticipación mínima de 2 horas y máxima de 30 días, calculadas en `America/Lima`.
- Se impiden coincidencias de veterinario o mascota entre citas RESERVADA y EN ESPERA. Una transacción bloquea los recursos antes de comprobar e insertar.
- El cliente cancela o reprograma hasta 2 horas antes. Recepción puede gestionar fuera de ese plazo; una nueva fecha siempre cumple el margen de reserva.
- Máximo 2 reprogramaciones. Solo se reprograma una cita RESERVADA; el paciente no cambia.
- Recepción confirma la llegada el día de la cita. Solo el veterinario asignado atiende una cita EN ESPERA.
- Recepción puede registrar NO ASISTIÓ desde los 15 minutos posteriores a la hora.
- Con 3 inasistencias en 90 días, el cliente reserva mediante recepción.
- Los estados ATENDIDA, CANCELADA y NO ASISTIÓ se conservan para consulta. La ficha clínica no tiene rutas de edición o eliminación.
- Los correos se **simulan en el log** (`NOTIFICACION_SIMULADA`); no se envían mensajes reales.

## Respaldo y restauración

Para PostgreSQL, usa `scripts/backup.sh archivo.dump` y `scripts/restore.sh archivo.dump base_vacia`. Requieren las herramientas cliente `pg_dump` y `pg_restore` de una versión compatible, y variables `PGHOST`, `PGPORT`, `PGUSER`, `PGPASSWORD`. El script de restauración escribe en otra base vacía; no elimina la base vigente. Verifica los datos restaurados antes de cambiar `DB_URL`.

En Windows puedes ejecutar los equivalentes `pg_dump --format=custom --no-owner --no-acl --file=archivo.dump` y `pg_restore --exit-on-error --no-owner --no-acl --dbname=base_vacia archivo.dump`, con las mismas variables PostgreSQL. Los scripts están incluidos; su ejecución y medición de restauración quedan pendientes en el equipo final.

En modo H2, detén la aplicación antes de copiar `data/`. Restaura esa copia con la aplicación detenida, conservando antes una copia de la carpeta vigente.

## Estructura

```text
src/main/java/pe/unsa/vetagenda/
  config/     Seguridad, reloj y carga inicial
  domain/     Tipos del dominio y errores de reglas
  service/    Agenda, citas, usuarios, inasistencias y temas
  web/        Controladores HTTP y manejo de errores
src/main/resources/
  db/migration/  Esquema versionado
  templates/     Pantallas Thymeleaf
  static/        Bootstrap se sirve desde el JAR; CSS y JS propios
config/theme/    Ejemplo de tema reemplazable
src/test/        Pruebas JUnit
scripts/         Arranque y respaldo/restauración
tests/selenium/ Suite funcional de navegador
 docs/           Guías, trazabilidad y evidencia real
 dist/           JAR local opcional, excluido de Git
```

## Alcance de esta versión

Es una entrega local v0.1.0. Incluye las funciones de los 12 RF del documento de producto y sus reglas RN-01 a RN-09. No incluye pagos, facturación, inventario, mensajería real ni despliegue público. Por defecto escucha únicamente en el equipo local.

Los resultados comprobados y lo pendiente están en [docs/VALIDACION.md](docs/VALIDACION.md). El código ejecutable y sus pruebas no sustituyen las entrevistas, la gestión real de cambios, el repositorio SVN, el análisis PILAR ni las mediciones JMeter que exige el curso. No se presentan esas evidencias como realizadas.

Se utilizó asistencia de IA para la implementación, pruebas y documentación. Las demostraciones contienen solo datos ficticios.

## Seguimiento académico

[Issues](https://github.com/DelCastilloBrad/VetAgenda-CaS2026/issues) contiene requisitos, cambios, defectos y pendientes de G01–G09. Consulta la [ERS de trabajo](docs/ERS.md), el [flujo y comparación de herramientas](docs/GESTION_REQUISITOS.md), las [solicitudes reales de cambio](docs/CAMBIOS.md) y el [índice de seguimiento](docs/SEGUIMIENTO.md). La [propuesta inicial](docs/PROPUESTA_INICIAL.md) se conserva como antecedente.
