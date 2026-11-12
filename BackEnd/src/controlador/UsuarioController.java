package controlador;

import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioController {

    // Metodo para registrar un usuario
    public boolean registrarUsuario(Usuario usuario) {

        String sql = "INSERT INTO usuarios (nombre_alias, contrasena, programa, rol) VALUES (?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

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

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                System.out.println("ID: " + resultado.getInt("id_usuario"));
                System.out.println("Nombre: " + resultado.getString("nombre_alias"));
                System.out.println("Programa: " + resultado.getString("programa"));
                System.out.println("-------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar usuarios: " + e.getMessage());
        }
    }
}