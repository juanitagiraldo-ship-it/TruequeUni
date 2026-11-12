
import controlador.TruequeController;
import modelo.Trueque;
import modelo.Usuario;
import modelo.Publicacion;

import java.util.Date;

public class PruebaTrueque {

    public static void main(String[] args) {

        TruequeController controlador = new TruequeController();

        Usuario usuario = new Usuario(
            6, "Prueba Trueque", "", "", "estudiante", 5.0
        );

        Publicacion publicacion = new Publicacion(
            5, "Libro de prueba", "", "", "", "", "",
            null, usuario, null
        );

        Trueque solicitud = new Trueque(
            0,
            new Date(),
            "pendiente",
            "intercambio",
            usuario,
            publicacion
        );

        controlador.registrarSolicitud(solicitud);
    }
}

