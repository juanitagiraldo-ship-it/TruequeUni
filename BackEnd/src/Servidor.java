
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import modelo.Usuario;
import modelo.ResultadoLogin;
import modelo.ResultadoRegistro;
import modelo.Publicacion;
import modelo.Categoria;
import sesion.SesionManager;

import controlador.LoginController;
import controlador.RegistroController;
import controlador.Catalogo;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;


public class Servidor {
    private static final SesionManager sesionManager = new SesionManager();
    public static void main(String[] args) {

        try {
            HttpServer servidor = HttpServer.create(
                new InetSocketAddress(8080), 0
            );

            // Mostrar las paginas del frontend
            servidor.createContext("/", intercambio -> {
                mostrarPagina(intercambio);
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

    private static void consultarCatalogo(HttpExchange intercambio)
            throws IOException {

        if (!intercambio.getRequestMethod().equalsIgnoreCase("GET")) {
            responder(intercambio, 405, "Metodo no permitido");
            return;
        }

        Catalogo catalogo = new Catalogo();

        ArrayList<Publicacion> publicaciones =
                catalogo.consultarPublicaciones();

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
