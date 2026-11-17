
package dao;

import conexion.Conexion;
import modelo.Usuario;
import modelo.ResultadoConsultaUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Accede a los datos de los usuarios en la base de datos.
 */
public class UsuarioDAO {

    /**
     * Registra un usuario en la base de datos.
     *
     * @param usuario usuario que se va a guardar
     * @return {@code true} si se insertó el usuario; {@code false} si no se
     *         pudo realizar el registro
     */
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

    /**
     * Obtiene un usuario por su identificador.
     */
    public ResultadoConsultaUsuario obtenerPorId(int idUsuario) {
        String sql = "SELECT id_usuario, nombre_alias, programa, rol, reputacion "
                   + "FROM usuarios WHERE id_usuario = ?";

        try (Connection conexion = Conexion.conectar()) {
            if (conexion == null) {
                return new ResultadoConsultaUsuario(
                        ResultadoConsultaUsuario.Estado.ERROR, null);
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
                consulta.setInt(1, idUsuario);
                try (ResultSet resultado = consulta.executeQuery()) {
                    if (!resultado.next()) {
                        return new ResultadoConsultaUsuario(
                                ResultadoConsultaUsuario.Estado.NO_ENCONTRADO,
                                null);
                    }

                    Usuario usuario = new Usuario(
                            resultado.getInt("id_usuario"),
                            resultado.getString("nombre_alias"),
                            "",
                            resultado.getString("programa"),
                            resultado.getString("rol"),
                            resultado.getDouble("reputacion"));
                    return new ResultadoConsultaUsuario(
                            ResultadoConsultaUsuario.Estado.ENCONTRADO,
                            usuario);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el perfil: " + e.getMessage());
            return new ResultadoConsultaUsuario(
                    ResultadoConsultaUsuario.Estado.ERROR, null);
        }
    }

    /**
     * Actualiza los campos editables del perfil, sin modificar ID ni reputación.
     *
     * @return 1 si se actualizó o no había cambios, 0 si no existe el usuario,
     *         o -1 si ocurrió un error
     */
    public int actualizarPerfil(
            int idUsuario,
            String nombreAlias,
            String programa,
            String rol,
            String nuevaContrasena) {

        boolean cambiarContrasena = nuevaContrasena != null
                && !nuevaContrasena.isEmpty();
        String sql = cambiarContrasena
                ? "UPDATE usuarios SET nombre_alias = ?, programa = ?, rol = ?, "
                    + "contrasena = ? WHERE id_usuario = ?"
                : "UPDATE usuarios SET nombre_alias = ?, programa = ?, rol = ? "
                    + "WHERE id_usuario = ?";

        try (Connection conexion = Conexion.conectar()) {
            if (conexion == null) {
                return -1;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
                consulta.setString(1, nombreAlias);
                consulta.setString(2, programa);
                consulta.setString(3, rol);
                if (cambiarContrasena) {
                    consulta.setString(4, nuevaContrasena);
                    consulta.setInt(5, idUsuario);
                } else {
                    consulta.setInt(4, idUsuario);
                }

                if (consulta.executeUpdate() > 0) {
                    return 1;
                }
                return existeUsuario(conexion, idUsuario) ? 1 : 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el perfil: " + e.getMessage());
            return -1;
        }
    }

    private boolean existeUsuario(Connection conexion, int idUsuario)
            throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idUsuario);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next();
            }
        }
    }

    /**
     * Busca un usuario que coincida con el alias y la contraseña indicados.
     *
     * @param nombreAlias alias del usuario
     * @param contrasena contraseña del usuario
     * @return resultado que indica si el usuario fue encontrado, no existe o
     *         ocurrió un error durante la consulta
     */
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
