#!/usr/bin/env sh
set -eu
if [ "$#" -ne 2 ]; then echo 'Uso: scripts/restore.sh archivo.dump nueva_base_vacia (PGUSER/PGPASSWORD en entorno)'; exit 2; fi
# Restore only into a separate, already created, empty database. No --clean or DROP.
pg_restore --exit-on-error --no-owner --no-acl --dbname="$2" "$1"
printf 'Restauración completada en %s. Verifica sus datos antes de cambiar DB_URL.\n' "$2"
