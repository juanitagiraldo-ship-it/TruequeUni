package controlador;

import modelo.Publicacion;
import modelo.Usuario;
import modelo.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

public class PublicacionController {

    // Metodo para registrar una publicacion
    public boolean registrarPublicacion(Publicacion publicacion) {

        String sql = "INSERT INTO publicaciones (titulo, descripcion, estado_fisico, estado_publicacion, tipo_oferta, imagen, fecha_publicacion, id_usuario, id_categoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, publicacion.getTitulo());
            consulta.setString(2, publicacion.getDescripcion());
            consulta.setString(3, publicacion.getEstadoFisico());
            consulta.setString(4, publicacion.getEstadoPublicacion());
            consulta.setString(5, publicacion.getTipoOferta());
            consulta.setString(6, publicacion.getImagen());

            if (publicacion.getFechaPublicacion() != null) {
                consulta.setDate(7, new Date(publicacion.getFechaPublicacion().getTime()));
            } else {
                consulta.setDate(7, null);
            }

            consulta.setInt(8, publicacion.getUsuario().getIdUsuario());
            consulta.setInt(9, publicacion.getCategoria().getIdCategoria());

            consulta.executeUpdate();

            System.out.println("Publicacion registrada correctamente");
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar publicacion: " + e.getMessage());
            return false;
        }
    }

    // Metodo para consultar todas las publicaciones
    public ArrayList<Publicacion> consultarPublicaciones() {

        ArrayList<Publicacion> publicaciones = new ArrayList<>();

        String sql = "SELECT * FROM publicaciones";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                Usuario usuario = new Usuario(
                    resultado.getInt("id_usuario"),
                    "", "", "", "", 0.0
                );

                Categoria categoria = new Categoria(
                    resultado.getInt("id_categoria"),
                    "",
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

        } catch (SQLException e) {
            System.out.println("Error al consultar publicaciones: " + e.getMessage());
        }

        return publicaciones;
    }
}