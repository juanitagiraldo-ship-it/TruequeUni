import controlador.UsuarioController;
import modelo.Usuario;

public class PruebaUsuario {
    
public static void main(String[] args) {

        UsuarioController controlador = new UsuarioController();

        System.out.println("LISTA DE USUARIOS REGISTRADOS");
        System.out.println("-----------------------------");

        controlador.consultarUsuarios();

    }
}
