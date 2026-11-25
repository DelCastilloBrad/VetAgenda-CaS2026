# Recorrido para presentar el producto

1. Inicia la aplicación en modo demo desde una carpeta nueva. Usa las cuentas del README.
2. **Cliente:** abre Mascotas, agrega un paciente, abre Reservar, selecciona servicio, veterinario y una fecha entre 2 horas y 30 días en día de atención. Busca horarios y confirma. Consulta Citas; prueba una reprogramación y una cancelación con una cita futura.
3. **Recepción:** abre Citas. La carga inicial contiene una cita para el día del primer arranque; usa **Confirmar llegada**. Una cita en espera queda disponible para el veterinario. **No asistió** solo se permite desde el minuto 15 posterior a la hora y sobre una cita aún reservada.
4. **Veterinario:** abre Citas y pulsa **Atender** en la cita en espera. Completa motivo, diagnóstico, tratamiento y peso. Guarda y consulta el historial. El estado pasa a ATENDIDA y la ficha queda de solo consulta.
5. **Administrador:** abre Reportes y elige el periodo. En Administración muestra usuarios internos, servicios, horarios y feriados. Explica que cerrar un día no elimina las citas previamente registradas.
6. Abre **Apariencia**, cambia el color principal y los bordes, guarda y recorre otra pantalla. Regresa y restablece el diseño. Para la edición técnica muestra `config/theme/custom.example.css` y la guía de personalización.
7. Muestra `docs/evidencias/cobertura/index.html` y los reportes JUnit. Explica qué se ejecutó y qué queda pendiente; no presentes la suite Selenium como aprobada hasta ejecutarla en tu equipo.

Los cambios sobre datos de demostración persisten. Para otra demostración limpia, descomprime nuevamente en otra carpeta. No borres ni sustituyas una base que contenga datos que quieras conservar.
