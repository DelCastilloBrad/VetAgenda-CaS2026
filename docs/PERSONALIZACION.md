# Personalizar VetAgenda

La apariencia no interviene en permisos, reglas de citas ni consultas SQL.

## Desde la aplicación

Con una cuenta ADMIN, abre **Apariencia**. Puedes cambiar color principal, fondo, tarjetas, texto, tipografía, escala (100/110/120 %) y bordes (0/8/16/24 px). Guarda para aplicar a todas las pantallas. Los valores se conservan en la base de datos. **Restablecer diseño** recupera los valores originales.

Se validan las opciones en servidor: el formulario no admite CSS ni JavaScript arbitrarios. Elige colores que permitan leer el texto y verifica las vistas móvil y escritorio; un valor de color válido no garantiza buen contraste entre todos los colores elegidos.

## Reemplazo mediante archivo

Copia `config/theme/custom.example.css` como `config/theme/custom.css`. El archivo se carga al final; no necesitas recompilar ni modificar plantillas. `THEME_DIRECTORY` permite ubicar esa carpeta fuera del proyecto. Es CSS de un mantenedor local de confianza, no una carga pública de archivos.

Orden de carga: Bootstrap local → `tokens.css` → `app.css` → ajustes de la base de datos → `custom.css`.

```css
:root {
  --va-primary: #315e9d;
  --va-on-primary: #ffffff;
  --va-bg: #f4f6fb;
  --va-radius: 8px;
}
```

| Variable | Controla |
|---|---|
| `--va-primary` / `--va-on-primary` | Color principal y texto sobre botones |
| `--va-bg` / `--va-surface` | Fondo general y tarjetas |
| `--va-text` / `--va-muted` | Texto principal y secundario |
| `--va-line` | Separadores y bordes |
| `--va-radius` | Radio de paneles y controles |
| `--va-font` / `--va-font-scale` | Familia y escala tipográfica |
| `--va-success` / `--va-warning` / `--va-danger` | Colores semánticos auxiliares |
| `--va-shadow` | Sombra de paneles |

Los colores de estado conservan el texto del estado: no se comunica información solamente por color. No cambies `display`, `position`, dimensiones de formularios, IDs, atributos `name`, `data-appointment-id`, `data-confirm`, rutas ni controles CSRF para crear un tema. Los tests buscan IDs, atributos y enlaces funcionales; no dependen de los colores.

Puedes reemplazar un tema completo cambiando únicamente `custom.css`. Para volver al diseño base, retira o vacía ese archivo y restaura el diseño desde Apariencia. El botón de restablecer no borra el archivo externo, que tiene prioridad; esto evita modificar archivos del mantenedor desde la interfaz.

Prueba cada tema en login, reserva, tabla de citas y formulario de atención, con anchos de 360 y 1440 px. Revisa foco visible, texto legible y controles accesibles con teclado.
