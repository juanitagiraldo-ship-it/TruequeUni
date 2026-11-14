package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;

public class Registro {

    private UsuarioDAO usuarioDAO;

    public Registro() {
        usuarioDAO = new UsuarioDAO();
    }

    public String registrarUsuario(String nombre, String contrasena) {

        if (nombre == null  nombre.trim().isEmpty()
                 contrasena == null || contrasena.trim().isEmpty()) {
            return "Por favor completa todos los campos.";
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
            return "Usuario registrado correctamente.";
        } else {
            return "No se pudo registrar el usuario.";
        }
    }
}