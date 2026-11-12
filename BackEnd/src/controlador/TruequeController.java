
package controlador;

import conexion.Conexion;
import modelo.Trueque;
import modelo.Usuario;
import modelo.Publicacion;
import modelo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

public class TruequeController {

    // Metodo para registrar una solicitud
    public boolean registrarSolicitud(Trueque trueque) {

        String sql = "INSERT INTO solicitudes (tipo_solicitud, estado, fecha_solicitud, id_usuario, id_publicacion) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, trueque.getTipoSolicitud());
            consulta.setString(2, trueque.getEstado());

            if (trueque.getFechaSolicitud() != null) {
                consulta.setDate(3, new Date(trueque.getFechaSolicitud().getTime()));
            } else {
                consulta.setDate(3, null);
            }

            consulta.setInt(4, trueque.getUsuario().getIdUsuario());
            consulta.setInt(5, trueque.getPublicacion().getIdPublicacion());

            consulta.executeUpdate();

            System.out.println("Solicitud registrada correctamente");
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar solicitud: " + e.getMessage());
            return false;
        }
    }

    // Metodo para consultar todas las solicitudes
    public ArrayList<Trueque> consultarSolicitudes() {

        ArrayList<Trueque> solicitudes = new ArrayList<>();

        String sql = "SELECT * FROM solicitudes";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                Usuario usuario = new Usuario(
                    resultado.getInt("id_usuario"),
                    "", "", "", "", 0.0
                );

                Publicacion publicacion = new Publicacion(
                    resultado.getInt("id_publicacion"),
                    "", "", "", "", "", "",
                    null, usuario, new Categoria(0, "", "")
                );

                Trueque trueque = new Trueque(
                    resultado.getInt("id_solicitud"),
                    resultado.getDate("fecha_solicitud"),
                    resultado.getString("estado"),
                    resultado.getString("tipo_solicitud"),
                    usuario,
                    publicacion
                );

                solicitudes.add(trueque);
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar solicitudes: " + e.getMessage());
        }

        return solicitudes;
    }

    // Metodo para actualizar el estado de una solicitud
    public boolean actualizarEstado(int idSolicitud, String nuevoEstado) {

        String sql = "UPDATE solicitudes SET estado = ? WHERE id_solicitud = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, nuevoEstado);
            consulta.setInt(2, idSolicitud);

            int filasActualizadas = consulta.executeUpdate();

            if (filasActualizadas > 0) {
                System.out.println("Estado de solicitud actualizado correctamente");
                return true;
            }

            System.out.println("No se encontro la solicitud");
            return false;

        } catch (SQLException e) {
            System.out.println("Error al actualizar solicitud: " + e.getMessage());
            return false;
        }
    }
}
