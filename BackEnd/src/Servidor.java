
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import modelo.Usuario;
import modelo.ResultadoLogin;
import modelo.ResultadoRegistro;
import modelo.ResultadoPublicacion;
import modelo.Publicacion;
import modelo.Categoria;
import sesion.SesionManager;

import controlador.LoginController;
import controlador.RegistroController;
import controlador.CatalogoController;
import controlador.PublicacionController;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;
import java.util.UUID;

import java.util.ArrayList;


public class Servidor {
    private static final SesionManager sesionManager = new SesionManager();
    private static final int MAX_IMAGEN_BYTES = 5 * 1024 * 1024;
    private static final int MAX_SOLICITUD_PUBLICACION_BYTES = 16 * 1024 * 1024;
    private static final Path DIRECTORIO_IMAGENES = Path.of(
            System.getProperty("java.io.tmpdir"),
            "truequeuni-uploads"
    ).toAbsolutePath().normalize();

    public static void main(String[] args) {

        try {
            HttpServer servidor = HttpServer.create(
                new InetSocketAddress(8080), 0
            );

            // Mostrar las paginas del frontend
            servidor.createContext("/", intercambio -> {
                mostrarPagina(intercambio);
            });

            servidor.createContext("/uploads/", intercambio -> {
                mostrarImagen(intercambio);
            });

            // Recibir registros desde el frontend
            servidor.createContext("/api/registro", intercambio -> {
                registrarUsuario(intercambio);
            });

            // Recibir inicios de sesion desde el frontend
            servidor.createContext("/api/login", intercambio -> {
                iniciarSesion(intercambio);
            });

            // Consultar publicaciones del catalogo
            servidor.createContext("/api/catalogo", intercambio -> {
            consultarCatalogo(intercambio);
            });

            servidor.createContext("/api/publicaciones", intercambio -> {
                registrarPublicacion(intercambio);
            });

            servidor.createContext("/api/mis-publicaciones", intercambio -> {
                consultarMisPublicaciones(intercambio);
            });

            servidor.createContext("/api/sesion", intercambio -> {

                Usuario usuario = obtenerUsuarioSesion(intercambio);

                if (usuario != null) {

                    responder(intercambio, 200,
                            "Sesion activa. Usuario: " + usuario.getNombreAlias()
                            + " | ID: " + usuario.getIdUsuario());

                } else {

                    responder(intercambio, 401, "No hay una sesion activa.");
                }
            });
            servidor.start();

            System.out.println("Servidor iniciado correctamente");
            System.out.println("Direccion: http://localhost:8080");

  

        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        }

        
    }

    // Metodo para mostrar archivos HTML
    private static void mostrarPagina(HttpExchange intercambio)
            throws IOException {

        Path carpetaFrontend = Path.of("FrontEnd")
                .toAbsolutePath()
                .normalize();

        String ruta = intercambio.getRequestURI().getPath();

        if (ruta.equals("/")) {
            ruta = "/index.html";
        }

        Path archivo = carpetaFrontend.resolve(ruta.substring(1))
                .normalize();

        if (!archivo.startsWith(carpetaFrontend) ||
            !Files.exists(archivo) ||
            !Files.isRegularFile(archivo)) {

            responder(intercambio, 404, "Pagina no encontrada");
            return;
        }

        byte[] contenido = Files.readAllBytes(archivo);

        String tipo = "text/html; charset=UTF-8";

        if (ruta.endsWith(".css")) {
            tipo = "text/css; charset=UTF-8";
        } else if (ruta.endsWith(".js")) {
            tipo = "application/javascript; charset=UTF-8";
        }

        intercambio.getResponseHeaders().set("Content-Type", tipo);
        intercambio.sendResponseHeaders(200, contenido.length);

        try (OutputStream salida = intercambio.getResponseBody()) {
            salida.write(contenido);
        }
    }

  // Metodo para registrar usuarios
    private static void registrarUsuario(HttpExchange intercambio)
            throws IOException {

        if (!intercambio.getRequestMethod().equalsIgnoreCase("POST")) {
            responder(intercambio, 405, "Metodo no permitido");
            return;
        }

        try {
            Map<String, String> formulario = obtenerDatosFormulario(intercambio);

            String nombre = formulario.get("nombre");
            String contrasena = formulario.get("contrasena");

           
            RegistroController registroController = new RegistroController();

            ResultadoRegistro resultado = registroController.registrarUsuario(
                    nombre, contrasena
            );

            if (resultado.getEstado() == ResultadoRegistro.Estado.EXITOSO) {

                responder(intercambio, 200, resultado.getMensaje());

            } else if (resultado.getEstado() == ResultadoRegistro.Estado.DATOS_INVALIDOS) {

                responder(intercambio, 400, resultado.getMensaje());

            } else {

                responder(intercambio, 500, resultado.getMensaje());
            }


        } catch (Exception e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            responder(intercambio, 500, "Error al procesar el registro");
        }
    }

    
    
  
// Metodo para iniciar sesion
private static void iniciarSesion(HttpExchange intercambio)
        throws IOException {

    if (!intercambio.getRequestMethod().equalsIgnoreCase("POST")) {
        responder(intercambio, 405, "Metodo no permitido");
        return;
    }

    try {
        Map<String, String> formulario = obtenerDatosFormulario(intercambio);

        String nombre = formulario.get("nombre");
        String contrasena = formulario.get("contrasena");

        LoginController loginController = new LoginController();

        ResultadoLogin resultado = loginController.iniciarSesion(nombre, contrasena);

 
        if (resultado.getEstado() == ResultadoLogin.Estado.EXITOSO) {

            Usuario usuario = resultado.getUsuario();

            String token = sesionManager.crearSesion(usuario);

            intercambio.getResponseHeaders().add(
                "Set-Cookie",
                "TRUEQUE_SESSION=" + token + "; Path=/; HttpOnly; SameSite=Strict"
            );

            responder(intercambio, 200, resultado.getMensaje());

        } else if (resultado.getEstado()  == ResultadoLogin.Estado.DATOS_INVALIDOS) {

            responder(intercambio, 400, resultado.getMensaje());

        } else if (resultado.getEstado() == ResultadoLogin.Estado.CREDENCIALES_INVALIDAS) {

            responder(intercambio, 401, resultado.getMensaje());

        } else {

            responder(intercambio, 500, resultado.getMensaje());
        }


    } catch (Exception e) {
        System.out.println("Error al iniciar sesion: " + e.getMessage());
        responder(intercambio, 500, "Error interno del servidor");
    }
}



    // Metodo para obtener los datos enviados por los formularios
    private static Map<String, String> obtenerDatosFormulario(
            HttpExchange intercambio) throws IOException {

        String datos = new String(
                intercambio.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        return analizarDatosFormulario(datos);
    }

    private static Map<String, String> obtenerDatosFormulario(
            HttpExchange intercambio, int maximoBytes) throws IOException {

        byte[] datos = intercambio.getRequestBody().readNBytes(maximoBytes + 1);
        if (datos.length > maximoBytes) {
            throw new SolicitudDemasiadoGrandeException();
        }

        return analizarDatosFormulario(
                new String(datos, StandardCharsets.UTF_8));
    }

    private static Map<String, String> analizarDatosFormulario(String datos) {
        Map<String, String> formulario = new HashMap<>();

        for (String campo : datos.split("&")) {

            String[] partes = campo.split("=", 2);

            if (partes.length == 2) {
                String clave = URLDecoder.decode(
                    partes[0], StandardCharsets.UTF_8
                );

                String valor = URLDecoder.decode(
                    partes[1], StandardCharsets.UTF_8
                );

                formulario.put(clave, valor);
            }
        }

        return formulario;
    }

    private static class SolicitudDemasiadoGrandeException extends IOException {
        private static final long serialVersionUID = 1L;
    }

    // Metodo para enviar respuestas al frontend
    private static void responder(
            HttpExchange intercambio, int codigo, String mensaje)
            throws IOException {

        byte[] respuesta = mensaje.getBytes(StandardCharsets.UTF_8);

        intercambio.getResponseHeaders().set(
            "Content-Type", "text/plain; charset=UTF-8"
        );

        intercambio.sendResponseHeaders(codigo, respuesta.length);

        try (OutputStream salida = intercambio.getResponseBody()) {
            salida.write(respuesta);
        }
    }
        // Metodo para obtener el usuario que inicio sesion
    
    private static Usuario obtenerUsuarioSesion(HttpExchange intercambio) {

        String cookies = intercambio.getRequestHeaders().getFirst("Cookie");

        if (cookies == null) {
            return null;
        }

        for (String cookie : cookies.split(";")) {

            cookie = cookie.trim();

            if (cookie.startsWith("TRUEQUE_SESSION=")) {

                String token = cookie.substring("TRUEQUE_SESSION=".length());

                return sesionManager.obtenerUsuario(token);
            }
        }

        return null;
    }

    private static void registrarPublicacion(HttpExchange intercambio)
            throws IOException {

        String ruta = intercambio.getRequestURI().getPath();
        if (ruta.matches("/api/publicaciones/[0-9]+")) {
            int idPublicacion;
            try {
                idPublicacion = Integer.parseInt(
                        ruta.substring("/api/publicaciones/".length()));
            } catch (NumberFormatException e) {
                responder(intercambio, 400, "El identificador no es válido.");
                return;
            }
            if (intercambio.getRequestMethod().equalsIgnoreCase("PATCH")) {
                actualizarPublicacion(intercambio, idPublicacion);
            } else if (intercambio.getRequestMethod().equalsIgnoreCase("DELETE")) {
                desactivarPublicacion(intercambio, idPublicacion);
            } else {
                responder(intercambio, 405, "Metodo no permitido");
            }
            return;
        }

        if (!ruta.equals("/api/publicaciones")) {
            responder(intercambio, 404, "Ruta no encontrada");
            return;
        }

        if (!intercambio.getRequestMethod().equalsIgnoreCase("POST")) {
            responder(intercambio, 405, "Metodo no permitido");
            return;
        }

        Usuario usuario = obtenerUsuarioSesion(intercambio);
        if (usuario == null) {
            responder(intercambio, 401, "Debes iniciar sesion para publicar.");
            return;
        }

        String rutaImagen = null;
        try {
            String tipoContenido =
                    intercambio.getRequestHeaders().getFirst("Content-Type");
            if (tipoContenido == null
                    || !tipoContenido.toLowerCase().startsWith(
                            "application/x-www-form-urlencoded")) {
                responder(intercambio, 415, "Formato de formulario no soportado.");
                return;
            }

            Map<String, String> formulario = obtenerDatosFormulario(
                    intercambio, MAX_SOLICITUD_PUBLICACION_BYTES);

            int idCategoria;
            try {
                idCategoria = Integer.parseInt(formulario.get("idCategoria"));
            } catch (NumberFormatException e) {
                responder(intercambio, 400, "Selecciona una categoria valida.");
                return;
            }

            rutaImagen = guardarImagenTemporal(
                    formulario.get("imagenBase64"),
                    formulario.get("tipoImagen")
            );

            PublicacionController publicacionController =
                    new PublicacionController();
            ResultadoPublicacion resultado =
                    publicacionController.registrarPublicacion(
                            formulario.get("titulo"),
                            formulario.get("descripcion"),
                            formulario.get("estadoFisico"),
                            formulario.get("tipoOferta"),
                            rutaImagen,
                            idCategoria,
                            usuario
                    );

            if (resultado.getEstado()
                    == ResultadoPublicacion.Estado.EXITOSO) {
                responder(intercambio, 201, resultado.getMensaje());
            } else {
                eliminarImagenTemporal(rutaImagen);
                int codigo = resultado.getEstado()
                        == ResultadoPublicacion.Estado.DATOS_INVALIDOS
                        ? 400 : 500;
                responder(intercambio, codigo, resultado.getMensaje());
            }

        } catch (SolicitudDemasiadoGrandeException e) {
            eliminarImagenTemporal(rutaImagen);
            responder(intercambio, 413, "La solicitud supera el tamaño permitido.");
        } catch (IllegalArgumentException e) {
            eliminarImagenTemporal(rutaImagen);
            responder(intercambio, 400, e.getMessage());
        } catch (IOException e) {
            eliminarImagenTemporal(rutaImagen);
            System.out.println("Error al procesar la publicación: " + e.getMessage());
            responder(intercambio, 500, "No se pudo procesar la publicación.");
        }
    }

    private static void actualizarPublicacion(
            HttpExchange intercambio, int idPublicacion) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(intercambio);
        if (usuario == null) {
            responder(intercambio, 401, "Debes iniciar sesion.");
            return;
        }

        String rutaImagen = null;
        try {
            String tipoContenido =
                    intercambio.getRequestHeaders().getFirst("Content-Type");
            if (tipoContenido == null
                    || !tipoContenido.toLowerCase().startsWith(
                            "application/x-www-form-urlencoded")) {
                responder(intercambio, 415, "Formato de formulario no soportado.");
                return;
            }

            Map<String, String> formulario = obtenerDatosFormulario(
                    intercambio, MAX_SOLICITUD_PUBLICACION_BYTES);
            int idCategoria;
            try {
                idCategoria = Integer.parseInt(formulario.get("idCategoria"));
            } catch (NumberFormatException e) {
                responder(intercambio, 400, "Selecciona una categoria valida.");
                return;
            }

            boolean eliminarImagen = "true".equals(
                    formulario.get("eliminarImagen"));
            if (!eliminarImagen) {
                rutaImagen = guardarImagenTemporal(
                        formulario.get("imagenBase64"),
                        formulario.get("tipoImagen"));
            }

            ResultadoPublicacion resultado = new PublicacionController()
                    .actualizarPublicacion(
                            idPublicacion,
                            formulario.get("titulo"),
                            formulario.get("descripcion"),
                            formulario.get("estadoFisico"),
                            formulario.get("tipoOferta"),
                            rutaImagen,
                            eliminarImagen,
                            idCategoria,
                            usuario);

            responderResultadoPublicacion(intercambio, resultado, rutaImagen);
        } catch (SolicitudDemasiadoGrandeException e) {
            eliminarImagenTemporal(rutaImagen);
            responder(intercambio, 413, "La solicitud supera el tamaño permitido.");
        } catch (IllegalArgumentException e) {
            eliminarImagenTemporal(rutaImagen);
            responder(intercambio, 400, e.getMessage());
        } catch (IOException e) {
            eliminarImagenTemporal(rutaImagen);
            System.out.println("Error al actualizar la publicación: "
                    + e.getMessage());
            responder(intercambio, 500, "No se pudo actualizar la publicación.");
        }
    }

    private static void desactivarPublicacion(
            HttpExchange intercambio, int idPublicacion) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(intercambio);
        if (usuario == null) {
            responder(intercambio, 401, "Debes iniciar sesion.");
            return;
        }

        ResultadoPublicacion resultado = new PublicacionController()
                .marcarComoInactiva(idPublicacion, usuario.getIdUsuario());
        responderResultadoPublicacion(intercambio, resultado, null);
    }

    private static void responderResultadoPublicacion(
            HttpExchange intercambio,
            ResultadoPublicacion resultado,
            String rutaImagen) throws IOException {
        if (resultado.getEstado() == ResultadoPublicacion.Estado.EXITOSO) {
            responder(intercambio, 200, resultado.getMensaje());
            return;
        }

        eliminarImagenTemporal(rutaImagen);
        int codigo = resultado.getEstado()
                == ResultadoPublicacion.Estado.DATOS_INVALIDOS ? 400
                : resultado.getEstado()
                        == ResultadoPublicacion.Estado.NO_ENCONTRADO ? 404 : 500;
        responder(intercambio, codigo, resultado.getMensaje());
    }

    private static String guardarImagenTemporal(
            String imagenBase64, String tipoImagen) throws IOException {

        if (imagenBase64 == null || imagenBase64.isEmpty()) {
            return null;
        }

        long maximoBase64 = ((MAX_IMAGEN_BYTES + 2L) / 3L) * 4L;
        if (imagenBase64.length() > maximoBase64) {
            throw new IllegalArgumentException(
                    "La imagen no puede superar los 5 MB.");
        }

        byte[] contenido;
        try {
            contenido = Base64.getDecoder().decode(imagenBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("La imagen enviada no es valida.");
        }

        if (contenido.length == 0 || contenido.length > MAX_IMAGEN_BYTES) {
            throw new IllegalArgumentException(
                    "La imagen no puede superar los 5 MB.");
        }

        String extension = obtenerExtensionImagen(tipoImagen, contenido);
        String nombreArchivo = UUID.randomUUID().toString() + extension;
        Path archivo = DIRECTORIO_IMAGENES.resolve(nombreArchivo).normalize();

        if (!archivo.startsWith(DIRECTORIO_IMAGENES)) {
            throw new IllegalArgumentException("Nombre de imagen no valido.");
        }

        Files.createDirectories(DIRECTORIO_IMAGENES);
        Files.write(archivo, contenido);

        return "/uploads/" + nombreArchivo;
    }

    private static String obtenerExtensionImagen(
            String tipoImagen, byte[] contenido) {

        if ("image/png".equals(tipoImagen)
                && contenido.length >= 8
                && (contenido[0] & 0xFF) == 0x89
                && contenido[1] == 0x50
                && contenido[2] == 0x4E
                && contenido[3] == 0x47
                && contenido[4] == 0x0D
                && contenido[5] == 0x0A
                && contenido[6] == 0x1A
                && contenido[7] == 0x0A) {
            return ".png";
        }

        if ("image/jpeg".equals(tipoImagen)
                && contenido.length >= 3
                && (contenido[0] & 0xFF) == 0xFF
                && (contenido[1] & 0xFF) == 0xD8
                && (contenido[2] & 0xFF) == 0xFF) {
            return ".jpg";
        }

        if ("image/gif".equals(tipoImagen)
                && contenido.length >= 6) {
            String encabezado = new String(
                    contenido, 0, 6, StandardCharsets.US_ASCII);
            if ("GIF87a".equals(encabezado)
                    || "GIF89a".equals(encabezado)) {
                return ".gif";
            }
        }

        throw new IllegalArgumentException(
                "La imagen debe ser un archivo PNG, JPEG o GIF valido.");
    }

    private static void eliminarImagenTemporal(String rutaImagen) {
        if (rutaImagen == null || !rutaImagen.startsWith("/uploads/")) {
            return;
        }

        String nombreArchivo = rutaImagen.substring("/uploads/".length());
        if (!nombreArchivo.matches(
                "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.(png|jpg|gif)")) {
            return;
        }

        try {
            Files.deleteIfExists(
                    DIRECTORIO_IMAGENES.resolve(nombreArchivo).normalize());
        } catch (IOException e) {
            System.out.println("No se pudo eliminar la imagen temporal: "
                    + e.getMessage());
        }
    }

    private static void mostrarImagen(HttpExchange intercambio)
            throws IOException {

        if (!intercambio.getRequestMethod().equalsIgnoreCase("GET")) {
            responder(intercambio, 405, "Metodo no permitido");
            return;
        }

        String ruta = intercambio.getRequestURI().getPath();
        String nombreArchivo = ruta.substring("/uploads/".length());
        if (!nombreArchivo.matches(
                "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.(png|jpg|gif)")) {
            responder(intercambio, 404, "Imagen no encontrada");
            return;
        }

        Path archivo = DIRECTORIO_IMAGENES.resolve(nombreArchivo).normalize();
        if (!archivo.startsWith(DIRECTORIO_IMAGENES)
                || !Files.isRegularFile(archivo)) {
            responder(intercambio, 404, "Imagen no encontrada");
            return;
        }

        String tipo = nombreArchivo.endsWith(".png") ? "image/png"
                : nombreArchivo.endsWith(".gif") ? "image/gif" : "image/jpeg";
        byte[] contenido = Files.readAllBytes(archivo);
        intercambio.getResponseHeaders().set("Content-Type", tipo);
        intercambio.sendResponseHeaders(200, contenido.length);

        try (OutputStream salida = intercambio.getResponseBody()) {
            salida.write(contenido);
        }
    }

    private static void consultarMisPublicaciones(HttpExchange intercambio)
            throws IOException {

        if (!intercambio.getRequestURI().getPath()
                .equals("/api/mis-publicaciones")) {
            responder(intercambio, 404, "Ruta no encontrada");
            return;
        }

        if (!intercambio.getRequestMethod().equalsIgnoreCase("GET")) {
            responder(intercambio, 405, "Metodo no permitido");
            return;
        }

        Usuario usuario = obtenerUsuarioSesion(intercambio);
        if (usuario == null) {
            responder(intercambio, 401, "Debes iniciar sesion.");
            return;
        }

        PublicacionController publicacionController =
                new PublicacionController();
        ArrayList<Publicacion> publicaciones =
                publicacionController.consultarPorUsuario(
                        usuario.getIdUsuario());

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < publicaciones.size(); i++) {
            Publicacion p = publicaciones.get(i);
            if (i > 0) {
                json.append(",");
            }

            json.append("{")
                .append("\"id\":").append(p.getIdPublicacion()).append(",")
                .append("\"titulo\":\"").append(escaparJSON(p.getTitulo())).append("\",")
                .append("\"descripcion\":\"").append(escaparJSON(p.getDescripcion())).append("\",")
                .append("\"estadoFisico\":\"").append(escaparJSON(p.getEstadoFisico())).append("\",")
                .append("\"estadoPublicacion\":\"").append(escaparJSON(p.getEstadoPublicacion())).append("\",")
                .append("\"tipoOferta\":\"").append(escaparJSON(p.getTipoOferta())).append("\",")
                .append("\"imagen\":\"").append(escaparJSON(p.getImagen())).append("\",")
                .append("\"idCategoria\":")
                .append(p.getCategoria().getIdCategoria()).append(",")
                .append("\"fechaPublicacion\":\"")
                .append(p.getFechaPublicacion() == null
                        ? "" : p.getFechaPublicacion().toString())
                .append("\",")
                .append("\"categoria\":\"")
                .append(escaparJSON(p.getCategoria().getNombreCategoria()))
                .append("\"}");
        }
        json.append("]");

        byte[] respuesta = json.toString().getBytes(StandardCharsets.UTF_8);
        intercambio.getResponseHeaders().set(
                "Content-Type", "application/json; charset=UTF-8");
        intercambio.sendResponseHeaders(200, respuesta.length);

        try (OutputStream salida = intercambio.getResponseBody()) {
            salida.write(respuesta);
        }
    }

    private static void consultarCatalogo(HttpExchange intercambio)
            throws IOException {

        if (!intercambio.getRequestMethod().equalsIgnoreCase("GET")) {
            responder(intercambio, 405, "Metodo no permitido");
            return;
        }

        CatalogoController catalogoController = new CatalogoController();

        String consulta = intercambio.getRequestURI().getRawQuery();
        Map<String, String> parametros = analizarDatosFormulario(
                consulta == null ? "" : consulta);
        String busqueda = parametros.get("busqueda");

        ArrayList<Publicacion> publicaciones =
                catalogoController.consultarPublicaciones(busqueda);

        StringBuilder json = new StringBuilder("[");
        
        for (int i = 0; i < publicaciones.size(); i++) {

            Publicacion p = publicaciones.get(i);
            Usuario u = p.getUsuario();
            Categoria c = p.getCategoria();

            if (i > 0) {
                json.append(",");
            }

            json.append("{")
                .append("\"id\":").append(p.getIdPublicacion()).append(",")
                .append("\"titulo\":\"").append(escaparJSON(p.getTitulo())).append("\",")
                .append("\"descripcion\":\"").append(escaparJSON(p.getDescripcion())).append("\",")
                .append("\"estadoFisico\":\"").append(escaparJSON(p.getEstadoFisico())).append("\",")
                .append("\"tipoOferta\":\"").append(escaparJSON(p.getTipoOferta())).append("\",")
                .append("\"imagen\":\"").append(escaparJSON(p.getImagen())).append("\",")
                .append("\"nombreUsuario\":\"").append(escaparJSON(u.getNombreAlias())).append("\",")
                .append("\"programa\":\"").append(escaparJSON(u.getPrograma())).append("\",")
                .append("\"reputacion\":").append(u.getReputacion()).append(",")
                .append("\"categoria\":\"").append(escaparJSON(c.getNombreCategoria())).append("\"")
                .append("}");
        }

        json.append("]");

        intercambio.getResponseHeaders().set(
            "Content-Type", "application/json; charset=UTF-8"
        );

        byte[] respuesta = json.toString().getBytes(StandardCharsets.UTF_8);

        intercambio.sendResponseHeaders(200, respuesta.length);

        try (OutputStream salida = intercambio.getResponseBody()) {
            salida.write(respuesta);
        }
    }

    private static String escaparJSON(String texto) {

        if (texto == null) {
            return "";
        }

        return texto.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
