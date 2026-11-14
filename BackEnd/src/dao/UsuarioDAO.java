
package dao;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean validarCredenciales(String nombreAlias, String contrasena) {

        String sql = "SELECT 1 FROM usuarios WHERE nombre_alias = ? AND contrasena = ? LIMIT 1";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return false;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setString(1, nombreAlias);
                consulta.setString(2, contrasena);

                try (ResultSet resultado = consulta.executeQuery()) {
                    return resultado.next();
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al validar el usuario: " + e.getMessage());
            return false;
        }
    }
}
