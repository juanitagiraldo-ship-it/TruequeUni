
package controlador;

import dao.UsuarioDAO;

public class Login {

    private UsuarioDAO usuarioDAO;

    public Login() {
        usuarioDAO = new UsuarioDAO();
    }

    public String iniciarSesion(String nombreAlias, String contrasena) {

        if (nombreAlias == null || nombreAlias.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {

            return "Por favor completa todos los campos.";
        }

        boolean usuarioValido = usuarioDAO.validarCredenciales(
                nombreAlias.trim(), contrasena
        );

        if (usuarioValido) {
            return "Inicio de sesion exitoso.";
        } else {
            return "El usuario o la contrasenia no coinciden.";
        }
    }
}
