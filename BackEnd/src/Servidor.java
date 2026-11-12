
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import controlador.UsuarioController;
import modelo.Usuario;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Servidor {

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

        Path carpetaFrontend = Path.of("FrontEnd").toAbsolutePath().normalize();

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
            String datos = new String(
                intercambio.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
            );

            Map<String, String> formulario = new HashMap<>();

            for (String campo : datos.split("&")) {
                String[] partes = campo.split("=", 2);

                if (partes.length == 2) {
                    formulario.put(
                        URLDecoder.decode(partes[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(partes[1], StandardCharsets.UTF_8)
                    );
                }
            }

            String nombre = formulario.get("nombre");
            String contrasena = formulario.get("contrasena");

            if (nombre == null || nombre.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty()) {

                responder(intercambio, 400, "Completa todos los campos");
                return;
            }

            Usuario usuario = new Usuario(
                0,
                nombre.trim(),
                contrasena,
                "",
                "estudiante",
                5.0
            );

            UsuarioController controlador = new UsuarioController();

            if (controlador.registrarUsuario(usuario)) {
                responder(intercambio, 200, "Usuario registrado correctamente");
            } else {
                responder(intercambio, 500, "No se pudo registrar el usuario");
            }

        } catch (Exception e) {
            responder(intercambio, 500, "Error al procesar el registro");
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Metodo para enviar respuestas
    private static void responder(
        HttpExchange intercambio, int codigo, String mensaje
    ) throws IOException {

        byte[] respuesta = mensaje.getBytes(StandardCharsets.UTF_8);

        intercambio.getResponseHeaders().set(
            "Content-Type", "text/plain; charset=UTF-8"
        );

        intercambio.sendResponseHeaders(codigo, respuesta.length);

        try (OutputStream salida = intercambio.getResponseBody()) {
            salida.write(respuesta);
        }
    }
}
