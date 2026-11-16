package controlador;

import dao.PublicacionDAO;
import modelo.Publicacion;
import java.util.ArrayList;

/**
 * Gestiona las operaciones de consulta del catálogo de publicaciones.
 */
public class Catalogo {

    private PublicacionDAO publicacionDAO;

    /**
     * Crea el catálogo e inicializa el acceso a los datos de publicaciones.
     */
    public Catalogo() {
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