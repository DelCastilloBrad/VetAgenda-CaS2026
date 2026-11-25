#!/usr/bin/env sh
set -eu
: "${PGHOST:=localhost}" "${PGPORT:=5432}" "${PGUSER:=vetagenda}" "${PGDATABASE:=vetagenda}"
export PGHOST PGPORT PGUSER PGDATABASE
if [ "$#" -ne 1 ]; then echo 'Uso: scripts/backup.sh archivo.dump (PGPASSWORD en entorno)'; exit 2; fi
umask 077
pg_dump --format=custom --no-owner --no-acl --file="$1"
pg_restore --list "$1" > /dev/null
printf 'Respaldo creado y catálogo verificado: %s\n' "$1"
