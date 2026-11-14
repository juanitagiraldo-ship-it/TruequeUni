package controlador;

import dao.PublicacionDAO;
import modelo.Publicacion;
import java.util.ArrayList;

public class Catalogo {

    private PublicacionDAO publicacionDAO;

    public Catalogo() {
        publicacionDAO = new PublicacionDAO();
    }

    // Metodo para obtener las publicaciones del catalogo
    public ArrayList<Publicacion> consultarPublicaciones() {

        return publicacionDAO.consultarPublicaciones();
    }
}