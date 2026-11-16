
package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import modelo.ResultadoRegistro;

public class RegistroController {

    private UsuarioDAO usuarioDAO;

    public RegistroController() {
        usuarioDAO = new UsuarioDAO();
    }

    public ResultadoRegistro registrarUsuario(String nombre, String contrasena) {

        if (nombre == null || nombre.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {

            return new ResultadoRegistro(ResultadoRegistro.Estado.DATOS_INVALIDOS, "Por favor completa todos los campos.");
        }

        Usuario usuario = new Usuario(
            0,
            nombre.trim(),
            contrasena,
            "",
            "estudiante",
            5.0
        );

        boolean registrado = usuarioDAO.registrarUsuario(usuario);

        if (registrado) {

            return new ResultadoRegistro(ResultadoRegistro.Estado.EXITOSO, "Usuario registrado correctamente.");

        } else {

            return new ResultadoRegistro(ResultadoRegistro.Estado.ERROR,"No se pudo registrar el usuario.");
        }
    }
}
