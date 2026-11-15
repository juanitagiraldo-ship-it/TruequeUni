
package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import modelo.ResultadoLogin;

public class LoginController {

    private UsuarioDAO usuarioDAO;

    public LoginController() {
        usuarioDAO = new UsuarioDAO();
    }

    public ResultadoLogin iniciarSesion(String nombreAlias, String contrasena) {

        if (nombreAlias == null || nombreAlias.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {

            return new ResultadoLogin(
                false,
                "Por favor completa todos los campos.",
                null
            );
        }

        Usuario usuario = usuarioDAO.obtenerPorCredenciales(
                nombreAlias.trim(), contrasena
        );

        if (usuario != null) {

            return new ResultadoLogin(
                true,
                "Inicio de sesión exitoso.",
                usuario
            );

        } else {

            return new ResultadoLogin(
                false,
                "El usuario o la contraseña no coinciden.",
                null
            );
        }
    }
}
