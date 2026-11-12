
package controlador;

import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import conexion.Conexion;

public class UsuarioController {


    // Metodo para validar el inicio de sesion
    public boolean validarInicioSesion(String nombreAlias, String contrasena) {

        String sql = "SELECT * FROM usuarios WHERE nombre_alias = ? AND contrasena = ?";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                System.out.println("No se pudo conectar con MySQL");
                return false;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setString(1, nombreAlias);
                consulta.setString(2, contrasena);

                try (ResultSet resultado = consulta.executeQuery()) {

                    if (resultado.next()) {
                        System.out.println("Inicio de sesion correcto");
                        return true;
                    }

                    System.out.println("Usuario o contraseña incorrectos");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al validar el usuario: " + e.getMessage());
            return false;
        }
    }


    // Metodo para registrar un usuario
    public boolean registrarUsuario(Usuario usuario) {

        if (usuario == null ||
            usuario.getNombreAlias() == null ||
            usuario.getNombreAlias().trim().isEmpty() ||
            usuario.getContrasena() == null ||
            usuario.getContrasena().trim().isEmpty()) {

            System.out.println("Debe ingresar un nombre y una contraseña");
            return false;
        }

        String sql = "INSERT INTO usuarios (nombre_alias, contrasena, programa, rol) VALUES (?, ?, ?, ?)";

        Connection conexion = Conexion.conectar();

        if (conexion == null) {
            System.out.println("No se pudo conectar con MySQL");
            return false;
        }

        try (Connection conn = conexion;
             PreparedStatement consulta = conn.prepareStatement(sql)) {

            consulta.setString(1, usuario.getNombreAlias());
            consulta.setString(2, usuario.getContrasena());
            consulta.setString(3, usuario.getPrograma());
            consulta.setString(4, usuario.getRol());

            consulta.executeUpdate();

            System.out.println("Usuario registrado correctamente");
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    // Metodo para consultar los usuarios
    public void consultarUsuarios() {

        String sql = "SELECT * FROM usuarios";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                System.out.println("No se pudo conectar con MySQL");
                return;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql);
                 ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {

                    System.out.println("ID: " + resultado.getInt("id_usuario"));
                    System.out.println("Nombre: " + resultado.getString("nombre_alias"));
                    System.out.println("Programa: " + resultado.getString("programa"));
                    System.out.println("-------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar usuarios: " + e.getMessage());
        }
    }
}
