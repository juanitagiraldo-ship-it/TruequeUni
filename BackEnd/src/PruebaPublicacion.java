
import controlador.PublicacionController;
import modelo.Publicacion;
import java.util.ArrayList;

public class PruebaPublicacion {

    public static void main(String[] args) {

        PublicacionController controlador = new PublicacionController();

        ArrayList<Publicacion> publicaciones =
                controlador.consultarPublicaciones();

        System.out.println("PUBLICACIONES REGISTRADAS");
        System.out.println("-------------------------");

        for (Publicacion publicacion : publicaciones) {

            System.out.println("ID: " + publicacion.getIdPublicacion());
            System.out.println("Titulo: " + publicacion.getTitulo());
            System.out.println("Descripcion: " + publicacion.getDescripcion());
            System.out.println("Estado: " + publicacion.getEstadoFisico());
            System.out.println("Tipo de oferta: " + publicacion.getTipoOferta());
            System.out.println("-------------------------");
        }

        System.out.println("Total de publicaciones: " + publicaciones.size());
    }
}
