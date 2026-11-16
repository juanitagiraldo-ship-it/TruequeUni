package controlador;

import dao.PublicacionDAO;
import modelo.ResultadoPublicacion;
import modelo.Publicacion;
import modelo.Usuario;
import modelo.Categoria;
import java.util.ArrayList;

/**
 * Coordina el registro y la consulta de publicaciones.
 */
public class PublicacionController {

    private final PublicacionDAO publicacionDAO;

    public PublicacionController() {
        publicacionDAO = new PublicacionDAO();
    }

    /**
     * Valida y registra una publicación nueva del usuario autenticado.
     */
    public ResultadoPublicacion registrarPublicacion(
            String titulo,
            String descripcion,
            String estadoFisico,
            String tipoOferta,
            String imagen,
            int idCategoria,
            Usuario usuario) {

        if (titulo == null || titulo.trim().isEmpty()
                || estadoFisico == null || estadoFisico.trim().isEmpty()
                || !("donacion".equals(tipoOferta)
                    || "intercambio".equals(tipoOferta))
                || idCategoria <= 0
                || usuario == null) {
            return new ResultadoPublicacion(
                    ResultadoPublicacion.Estado.DATOS_INVALIDOS,
                    "Completa los campos obligatorios de la publicación.");
        }

        Publicacion publicacion = new Publicacion(
            0,
            titulo.trim(),
            descripcion == null ? "" : descripcion.trim(),
            estadoFisico.trim(),
            "activo",
            tipoOferta,
            imagen,
            new java.util.Date(),
            usuario,
            new Categoria(idCategoria, "", "")
        );

        if (!publicacionDAO.registrarPublicacion(publicacion)) {
            return new ResultadoPublicacion(
                    ResultadoPublicacion.Estado.ERROR,
                    "No se pudo registrar la publicación.");
        }

        return new ResultadoPublicacion(
                ResultadoPublicacion.Estado.EXITOSO,
                "Publicación registrada correctamente.");
    }

    /**
     * Consulta las publicaciones del usuario autenticado.
     */
    public ArrayList<Publicacion> consultarPorUsuario(int idUsuario) {
        if (idUsuario <= 0) {
            return new ArrayList<>();
        }
        return publicacionDAO.consultarPorUsuario(idUsuario);
    }

    /**
     * Actualiza los datos editables sin modificar el estado de publicación.
     */
    public ResultadoPublicacion actualizarPublicacion(
            int idPublicacion,
            String titulo,
            String descripcion,
            String estadoFisico,
            String tipoOferta,
            String imagen,
            boolean eliminarImagen,
            int idCategoria,
            Usuario usuario) {

        if (idPublicacion <= 0 || titulo == null || titulo.trim().isEmpty()
                || estadoFisico == null || estadoFisico.trim().isEmpty()
                || !("donacion".equals(tipoOferta)
                    || "intercambio".equals(tipoOferta))
                || idCategoria <= 0 || usuario == null) {
            return new ResultadoPublicacion(
                    ResultadoPublicacion.Estado.DATOS_INVALIDOS,
                    "Completa los campos obligatorios de la publicación.");
        }

        Publicacion publicacion = new Publicacion(
                idPublicacion,
                titulo.trim(),
                descripcion == null ? "" : descripcion.trim(),
                estadoFisico.trim(),
                null,
                tipoOferta,
                imagen,
                null,
                usuario,
                new Categoria(idCategoria, "", "")
        );

        int filasActualizadas = publicacionDAO.actualizarPublicacion(
                publicacion, eliminarImagen);
        if (filasActualizadas < 0) {
            return new ResultadoPublicacion(
                    ResultadoPublicacion.Estado.ERROR,
                    "No se pudo actualizar la publicación.");
        }
        if (filasActualizadas == 0) {
            return new ResultadoPublicacion(
                    ResultadoPublicacion.Estado.NO_ENCONTRADO,
                    "No se encontró una publicación editable para este usuario.");
        }

        return new ResultadoPublicacion(
                ResultadoPublicacion.Estado.EXITOSO,
                "Publicación actualizada correctamente.");
    }

    /**
     * Da de baja una publicación sin eliminarla de la base de datos.
     */
    public ResultadoPublicacion marcarComoInactiva(
            int idPublicacion, int idUsuario) {
        if (idPublicacion <= 0 || idUsuario <= 0) {
            return new ResultadoPublicacion(
                    ResultadoPublicacion.Estado.DATOS_INVALIDOS,
                    "La publicación indicada no es válida.");
        }

        int filasActualizadas = publicacionDAO.marcarComoInactiva(
                idPublicacion, idUsuario);
        if (filasActualizadas < 0) {
            return new ResultadoPublicacion(
                    ResultadoPublicacion.Estado.ERROR,
                    "No se pudo desactivar la publicación.");
        }
        if (filasActualizadas == 0) {
            return new ResultadoPublicacion(
                    ResultadoPublicacion.Estado.NO_ENCONTRADO,
                    "No se encontró una publicación activa para este usuario.");
        }

        return new ResultadoPublicacion(
                ResultadoPublicacion.Estado.EXITOSO,
                "Publicación marcada como inactiva.");
    }
}