package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;

public class Login {

    private UsuarioDAO usuarioDAO;

    public Login() {
        usuarioDAO = new UsuarioDAO();
    }

    public Usuario iniciarSesion(String nombreAlias, String contrasena) {

        if (nombreAlias == null || nombreAlias.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {

            return null;
        }

        return usuarioDAO.obtenerPorCredenciales(
                nombreAlias.trim(), contrasena
        );
    }
}