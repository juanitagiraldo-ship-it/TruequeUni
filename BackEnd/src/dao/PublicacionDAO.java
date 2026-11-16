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
}