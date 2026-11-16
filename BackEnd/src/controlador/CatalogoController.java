package controlador;

import dao.PublicacionDAO;
import modelo.Publicacion;
import java.util.ArrayList;

/**
 * Coordina las consultas de publicaciones para el catálogo.
 */
public class CatalogoController {

    private PublicacionDAO publicacionDAO;

    /**
     * Crea el controlador e inicializa el acceso a los datos de publicaciones.
     */
    public CatalogoController() {
        publicacionDAO = new PublicacionDAO();
    }

    /**
     * Obtiene las publicaciones disponibles en el catálogo.
     *
     * @return lista de publicaciones consultadas
     */
    public ArrayList<Publicacion> consultarPublicaciones() {

        return publicacionDAO.consultarPublicaciones();
    }
}