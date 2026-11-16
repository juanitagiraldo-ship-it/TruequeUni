package dao;

import conexion.Conexion;
import modelo.Publicacion;
import modelo.Usuario;
import modelo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Accede a los datos de las publicaciones.
 */
public class PublicacionDAO {

    /**
     * Registra una publicación asociada a un usuario y una categoría.
     *
     * @param publicacion publicación que se va a guardar
     * @return {@code true} si se insertó; {@code false} si no se pudo guardar
     */
    public boolean registrarPublicacion(Publicacion publicacion) {

        String sql = "INSERT INTO publicaciones "
                   + "(titulo, descripcion, estado_fisico, estado_publicacion, "
                   + "tipo_oferta, imagen, fecha_publicacion, id_usuario, id_categoria) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return false;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
                consulta.setString(1, publicacion.getTitulo());
                consulta.setString(2, publicacion.getDescripcion());
                consulta.setString(3, publicacion.getEstadoFisico());
                consulta.setString(4, publicacion.getEstadoPublicacion());
                consulta.setString(5, publicacion.getTipoOferta());
                consulta.setString(6, publicacion.getImagen());

                if (publicacion.getFechaPublicacion() != null) {
                    consulta.setDate(7, new java.sql.Date(
                            publicacion.getFechaPublicacion().getTime()));
                } else {
                    consulta.setDate(7, null);
                }

                consulta.setInt(8, publicacion.getUsuario().getIdUsuario());
                consulta.setInt(9, publicacion.getCategoria().getIdCategoria());

                return consulta.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar publicación: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los campos editables de una publicación perteneciente al usuario.
     *
     * @return cantidad de filas actualizadas, o -1 si ocurrió un error
     */
    public int actualizarPublicacion(
            Publicacion publicacion, boolean eliminarImagen) {

        String sql = "UPDATE publicaciones "
                   + "SET titulo = ?, descripcion = ?, estado_fisico = ?, "
                   + "tipo_oferta = ?, id_categoria = ?, "
                   + "imagen = CASE WHEN ? THEN NULL "
                   + "WHEN ? IS NOT NULL THEN ? ELSE imagen END "
                   + "WHERE id_publicacion = ? AND id_usuario = ?";

        try (Connection conexion = Conexion.conectar()) {
            if (conexion == null) {
                return -1;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
                consulta.setString(1, publicacion.getTitulo());
                consulta.setString(2, publicacion.getDescripcion());
                consulta.setString(3, publicacion.getEstadoFisico());
                consulta.setString(4, publicacion.getTipoOferta());
                consulta.setInt(5, publicacion.getCategoria().getIdCategoria());
                consulta.setBoolean(6, eliminarImagen);
                consulta.setString(7, publicacion.getImagen());
                consulta.setString(8, publicacion.getImagen());
                consulta.setInt(9, publicacion.getIdPublicacion());
                consulta.setInt(10, publicacion.getUsuario().getIdUsuario());
                int filasActualizadas = consulta.executeUpdate();
                return filasActualizadas > 0 || existePublicacionPropia(
                        conexion,
                        publicacion.getIdPublicacion(),
                        publicacion.getUsuario().getIdUsuario())
                        ? 1 : 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar publicación: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Marca como inactiva una publicación perteneciente al usuario.
     *
     * @return cantidad de filas actualizadas, o -1 si ocurrió un error
     */
    public int marcarComoInactiva(int idPublicacion, int idUsuario) {
        String sql = "UPDATE publicaciones SET estado_publicacion = 'inactivo' "
                   + "WHERE id_publicacion = ? AND id_usuario = ?";

        try (Connection conexion = Conexion.conectar()) {
            if (conexion == null) {
                return -1;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
                consulta.setInt(1, idPublicacion);
                consulta.setInt(2, idUsuario);
                int filasActualizadas = consulta.executeUpdate();
                return filasActualizadas > 0
                        || existePublicacionPropia(
                                conexion, idPublicacion, idUsuario)
                        ? 1 : 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al marcar publicación como inactiva: "
                    + e.getMessage());
            return -1;
        }
    }

    private boolean existePublicacionPropia(
            Connection conexion, int idPublicacion, int idUsuario)
            throws SQLException {
        String sql = "SELECT 1 FROM publicaciones "
                   + "WHERE id_publicacion = ? AND id_usuario = ?";
        try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idPublicacion);
            consulta.setInt(2, idUsuario);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next();
            }
        }
    }

    /**
     * Consulta las publicaciones activas, ordenadas de la más reciente a la
     * más antigua, junto con los datos asociados de usuario y categoría.
     *
     * @return lista de publicaciones activas; puede estar vacía si no hay
     *         resultados o si no se puede completar la consulta
     */
    public ArrayList<Publicacion> consultarPublicaciones() {

        ArrayList<Publicacion> publicaciones = new ArrayList<>();

        String sql = "SELECT p.*, u.nombre_alias, u.programa, u.reputacion, "
                   + "c.nombre_categoria "
                   + "FROM publicaciones p "
                   + "INNER JOIN usuarios u ON p.id_usuario = u.id_usuario "
                   + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                   + "WHERE p.estado_publicacion = 'activo' "
                   + "ORDER BY p.fecha_publicacion DESC";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return publicaciones;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql);
                 ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {

                    Usuario usuario = new Usuario(
                        resultado.getInt("id_usuario"),
                        resultado.getString("nombre_alias"),
                        "",
                        resultado.getString("programa"),
                        "",
                        resultado.getDouble("reputacion")
                    );

                    Categoria categoria = new Categoria(
                        resultado.getInt("id_categoria"),
                        resultado.getString("nombre_categoria"),
                        ""
                    );

                    Publicacion publicacion = new Publicacion(
                        resultado.getInt("id_publicacion"),
                        resultado.getString("titulo"),
                        resultado.getString("descripcion"),
                        resultado.getString("estado_fisico"),
                        resultado.getString("estado_publicacion"),
                        resultado.getString("tipo_oferta"),
                        resultado.getString("imagen"),
                        resultado.getDate("fecha_publicacion"),
                        usuario,
                        categoria
                    );

                    publicaciones.add(publicacion);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar publicaciones: " + e.getMessage());
        }

        return publicaciones;
    }

    /**
     * Consulta todas las publicaciones de un usuario, sin filtrar por estado.
     *
     * @param idUsuario propietario de las publicaciones
     * @return publicaciones del usuario; la lista puede estar vacía
     */
    public ArrayList<Publicacion> consultarPorUsuario(int idUsuario) {

        ArrayList<Publicacion> publicaciones = new ArrayList<>();

        String sql = "SELECT p.*, u.nombre_alias, u.programa, u.reputacion, "
                   + "c.nombre_categoria "
                   + "FROM publicaciones p "
                   + "INNER JOIN usuarios u ON p.id_usuario = u.id_usuario "
                   + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                   + "WHERE p.id_usuario = ? "
                   + "ORDER BY p.fecha_publicacion DESC";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return publicaciones;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
                consulta.setInt(1, idUsuario);

                try (ResultSet resultado = consulta.executeQuery()) {
                    while (resultado.next()) {
                        Usuario usuario = new Usuario(
                            resultado.getInt("id_usuario"),
                            resultado.getString("nombre_alias"),
                            "",
                            resultado.getString("programa"),
                            "",
                            resultado.getDouble("reputacion")
                        );

                        Categoria categoria = new Categoria(
                            resultado.getInt("id_categoria"),
                            resultado.getString("nombre_categoria"),
                            ""
                        );

                        Publicacion publicacion = new Publicacion(
                            resultado.getInt("id_publicacion"),
                            resultado.getString("titulo"),
                            resultado.getString("descripcion"),
                            resultado.getString("estado_fisico"),
                            resultado.getString("estado_publicacion"),
                            resultado.getString("tipo_oferta"),
                            resultado.getString("imagen"),
                            resultado.getDate("fecha_publicacion"),
                            usuario,
                            categoria
                        );

                        publicaciones.add(publicacion);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar publicaciones del usuario: "
                    + e.getMessage());
        }

        return publicaciones;
    }
}