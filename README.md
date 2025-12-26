# Ficheros
Esta aplicación gestiona un fichero de texto, `texto.txt`, al cual podremos añadir cadenas de texto, visualizar la ruta del fichero y pasarlo del almacenamiento interno al externo.
## Demo
Se puede ver la demo del proyecto en la carpeta de este zip como [demo_ficheros.mp4](demo_ficheros.mp4) Cualquier problema con la versión entregada por Moodle (tanto del proyecto como del README) se puede usar el repositorio donde se encuentra alojada la práctica: [https://github.com/solsolet/Ficheros.git](https://github.com/solsolet/Ficheros.git)

## Implementación
Primero se creó la base de la aplicación con layouts para las diferentes pantallas con los elementos requeridos. La aplicación consta de 3 actividades:
- `MainActivity`: permite introducir una cadena de texto y pulsar los botones para ver el estado el almacenamiento externo, el fichero, añadir el texto al fichero, mover el almacenamiento de externo a interno y cerrar la aplicación.
- `ContenidoFichero`: muestra el contenido del texto que introducimos que se va acumulando y tiene un botón para volver a _main_.
- `EstadoAlmacenamiento`: muestra el estado del almacenamiento de la aplicación, las rutas de los directorios... y tiene un botón para volver a _main_.

Anotación para los siguientes apartados: Todas las funciones vienen del package `es.ua.eps.ficheros`.

### Qué se ha hecho
- Implementada la aplicación en Java cumpliendo los requisitos:
  - Añadir texto al fichero sin sobrescribir (append).
  - Visualizar contenido del fichero.
  - Consultar estado del almacenamiento externo (valores de `Environment`).
  - Mover fichero entre almacenamiento interno y externo app-specific (copy + delete).
  - Interfaz con botones y estados que reflejan la ubicación del fichero.
- Uso de `getFilesDir()` y `getExternalFilesDir(null)` para ajustarse a Scoped Storage moderno (API 29+).

### Ficheros y funciones clave
- Activity principal: [app/src/main/java/es/ua/eps/ficheros/MainActivity.java](app/src/main/java/es/ua/eps/ficheros/MainActivity.java)
  - [`MainActivity.initListeners`](app/src/main/java/es/ua/eps/ficheros/MainActivity.java) — registra los listeners de los botones y acciones UI.
  - [`MainActivity.anyadeTexto`](app/src/main/java/es/ua/eps/ficheros/MainActivity.java) — abre un FileOutputStream en modo append y añade el texto al final del fichero.
  - [`MainActivity.moverExterno`](app/src/main/java/es/ua/eps/ficheros/MainActivity.java) — copia el fichero interno a `getExternalFilesDir(null)` y borra el original.
  - [`MainActivity.moverInterno`](app/src/main/java/es/ua/eps/ficheros/MainActivity.java) — copia el fichero externo app-specific a interno y borra el original.
  - [`MainActivity.copiarFichero`](app/src/main/java/es/ua/eps/ficheros/MainActivity.java) — copia byte[] entre streams (buffer 1024 bytes).
  - [`MainActivity.updateButtonState`](app/src/main/java/es/ua/eps/ficheros/MainActivity.java) — habilita/deshabilita botones según ubicación actual del fichero.
  - Navegación: [`MainActivity.irContenido`](app/src/main/java/es/ua/eps/ficheros/MainActivity.java) y [`MainActivity.irEstado`](app/src/main/java/es/ua/eps/ficheros/MainActivity.java) — crean Intents hacia las Activities correspondientes.
- Activity contenido: [app/src/main/java/es/ua/eps/ficheros/ContenidoFichero.java](app/src/main/java/es/ua/eps/ficheros/ContenidoFichero.java)
  - [`ContenidoFichero.cargarContenido`](app/src/main/java/es/ua/eps/ficheros/ContenidoFichero.java) — lee con BufferedReader y muestra contenido en `tvContenido`.
- Activity estado: [app/src/main/java/es/ua/eps/ficheros/EstadoAlmacenamiento.java](app/src/main/java/es/ua/eps/ficheros/EstadoAlmacenamiento.java)
  - [`EstadoAlmacenamiento.mostrarEstado`](app/src/main/java/es/ua/eps/ficheros/EstadoAlmacenamiento.java) — consulta `Environment.getExternalStorageState()` y muestra interpretación y rutas (interno y externo app-specific).

Recursos de texto: [app/src/main/res/values/strings.xml](app/src/main/res/values/strings.xml)

## Comprobaciones/Checklist (requeridas en la práctica)
- Escritura en el fichero: realizado por [`MainActivity.anyadeTexto`](app/src/main/java/es/ua/eps/ficheros/MainActivity.java).
- Visualización del contenido: realizado por [`ContenidoFichero.cargarContenido`](app/src/main/java/es/ua/eps/ficheros/ContenidoFichero.java).
- Consulta del estado del almacenamiento externo: realizado por [`EstadoAlmacenamiento.mostrarEstado`](app/src/main/java/es/ua/eps/ficheros/EstadoAlmacenamiento.java).
- Movimiento del fichero entre ubicaciones: realizado por [`MainActivity.moverExterno`](app/src/main/java/es/ua/eps/ficheros/MainActivity.java) y [`MainActivity.moverInterno`](app/src/main/java/es/ua/eps/ficheros/MainActivity.java).


## Problemas
El primer problema fue encontrar la versión para hacer la practica en Java ya que todo aparecía para Kotlin. Me decanté por los primeros resultados de búsqueda que era usar `OutputStream` e `InputStream` y todo lo relacionado con `File`. Una vez encontrado un punto de partida fue más fácil ver como resolver la práctica.

Otro problem ame sucedió con el `Intent`, no le pasaba datos adicionales para ir a `ContenidoFichero` y siempre saltaba el mensaje de error en lugar del texto que debería aparecer.