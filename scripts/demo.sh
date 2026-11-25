#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")/.."
if [ ! -f dist/vetagenda.jar ]; then mvn -B -DskipTests package; mkdir -p dist; cp target/vetagenda-0.1.0.jar dist/vetagenda.jar; fi
exec java -jar dist/vetagenda.jar --spring.profiles.active=demo
