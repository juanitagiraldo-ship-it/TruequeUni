
package dao;

import conexion.Conexion;
import modelo.Usuario;
import modelo.ResultadoConsultaUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // Metodo para registrar un usuario
    public boolean registrarUsuario(Usuario usuario) {

        String sql = "INSERT INTO usuarios (nombre_alias, contrasena, programa, rol, reputacion) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return false;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setString(1, usuario.getNombreAlias());
                consulta.setString(2, usuario.getContrasena());
                consulta.setString(3, usuario.getPrograma());
                consulta.setString(4, usuario.getRol());
                consulta.setDouble(5, usuario.getReputacion());

                return consulta.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    // Metodo para obtener un usuario mediante sus credenciales
   
    // Metodo para obtener un usuario mediante sus credenciales
    public ResultadoConsultaUsuario obtenerPorCredenciales(
            String nombreAlias, String contrasena) {

        String sql = "SELECT * FROM usuarios WHERE nombre_alias = ? AND contrasena = ? LIMIT 1";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return new ResultadoConsultaUsuario(
                    ResultadoConsultaUsuario.Estado.ERROR,
                    null
                );
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setString(1, nombreAlias);
                consulta.setString(2, contrasena);

                try (ResultSet resultado = consulta.executeQuery()) {

                    if (resultado.next()) {

                        Usuario usuario = new Usuario(
                            resultado.getInt("id_usuario"),
                            resultado.getString("nombre_alias"),
                            resultado.getString("contrasena"),
                            resultado.getString("programa"),
                            resultado.getString("rol"),
                            resultado.getDouble("reputacion")
                        );

                        return new ResultadoConsultaUsuario(
                            ResultadoConsultaUsuario.Estado.ENCONTRADO,
                            usuario
                        );
                    }

                    return new ResultadoConsultaUsuario(
                        ResultadoConsultaUsuario.Estado.NO_ENCONTRADO,
                        null
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener el usuario: " + e.getMessage());

            return new ResultadoConsultaUsuario(
                ResultadoConsultaUsuario.Estado.ERROR,
                null
            );
        }
    }

}
