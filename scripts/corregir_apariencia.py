#!/usr/bin/env python3
"""Corrige únicamente la lectura del color en la plantilla de VetAgenda 0.1.0.
Ejecutar desde la carpeta que contiene pom.xml y dist/vetagenda.jar, con Java detenido.
No requiere paquetes adicionales. Conserva una copia del JAR y no abre la base de datos.
"""
from pathlib import Path
from datetime import datetime, timezone
import hashlib
import json
import os
import shutil
import sys
import tempfile
import zipfile

ENTRY = 'BOOT-INF/classes/templates/appearance.html'
OLD = '${theme[key]}'
NEW = '${theme.get(key)}'


def sha256(path):
    h = hashlib.sha256()
    with path.open('rb') as source:
        for block in iter(lambda: source.read(1024 * 1024), b''):
            h.update(block)
    return h.hexdigest()


def main():
    root = Path.cwd()
    jar = root / 'dist/vetagenda.jar'
    source = root / 'src/main/resources/templates/appearance.html'
    if not (root / 'pom.xml').is_file() or not jar.is_file():
        raise RuntimeError('Ejecuta este archivo dentro de vetagenda, donde están pom.xml y dist/.')
    with zipfile.ZipFile(jar) as original:
        template = original.read(ENTRY).decode('utf-8')
    if OLD not in template:
        if NEW in template:
            print('La corrección de apariencia ya está aplicada. No se cambió el JAR.')
            return
        raise RuntimeError('La plantilla no coincide con la versión esperada. No se cambió ningún archivo.')
    if template.count(OLD) != 1:
        raise RuntimeError('Se esperaba una sola expresión de color. No se cambió ningún archivo.')
    source_text = source.read_text(encoding='utf-8') if source.is_file() else None
    if source_text is not None and OLD not in source_text and NEW not in source_text:
        raise RuntimeError('El fuente tiene otra estructura. No se cambió ningún archivo.')
    backup = jar.with_name('vetagenda.antes-corregir-apariencia.jar')
    if backup.exists():
        raise RuntimeError('Ya existe la copia de respaldo. Consérvala y revisa antes de repetir.')
    old_hash = sha256(jar)
    handle, temporary = tempfile.mkstemp(prefix='vetagenda-apariencia-', suffix='.jar', dir=jar.parent)
    os.close(handle)
    temp_path = Path(temporary)
    try:
        with zipfile.ZipFile(jar) as original, zipfile.ZipFile(temp_path, 'w') as fixed:
            fixed.comment = original.comment
            for item in original.infolist():
                content = original.read(item.filename)
                if item.filename == ENTRY:
                    content = template.replace(OLD, NEW, 1).encode('utf-8')
                # Preserve each compression method: nested Spring Boot JARs stay stored.
                fixed.writestr(item, content)
        with zipfile.ZipFile(temp_path) as check:
            if check.testzip() is not None:
                raise RuntimeError('Falló la comprobación del archivo corregido.')
            assert NEW in check.read(ENTRY).decode('utf-8')
        shutil.copystat(jar, temp_path)
        shutil.copy2(jar, backup)
        os.replace(temp_path, jar)
        if source_text is not None and OLD in source_text:
            source.write_text(source_text.replace(OLD, NEW, 1), encoding='utf-8')
        log = root / 'docs/correccion-apariencia.json'
        log.parent.mkdir(parents=True, exist_ok=True)
        log.write_text(json.dumps({
            'defecto': 'El selector de color se renderizaba con value vacío al cargar la página.',
            'cambio': 'Lectura explícita de la clave mediante theme.get(key).',
            'fecha_utc': datetime.now(timezone.utc).isoformat(),
            'jar_anterior_sha256': old_hash,
            'jar_corregido_sha256': sha256(jar),
            'copia_anterior': str(backup.relative_to(root)),
            'validacion_selenium': 'Pendiente de ejecutar en este equipo.'
        }, indent=2, ensure_ascii=False), encoding='utf-8')
        print('Corrección aplicada al ejecutable y a la plantilla fuente disponible.')
        print('Copia anterior: dist/vetagenda.antes-corregir-apariencia.jar')
        print('Reinicia la aplicación y ejecuta nuevamente Selenium.')
    finally:
        if temp_path.exists():
            temp_path.unlink()


if __name__ == '__main__':
    try:
        main()
    except (OSError, RuntimeError, zipfile.BadZipFile, KeyError) as error:
        print('No se pudo completar la corrección:', error, file=sys.stderr)
        sys.exit(1)
