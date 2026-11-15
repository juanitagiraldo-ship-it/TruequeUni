
package sesion;

import modelo.Usuario;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SesionManager {

    private static final Map<String, Usuario> sesiones =
            new ConcurrentHashMap<>();

    // Metodo para crear una sesion
    public String crearSesion(Usuario usuario) {

        String token = UUID.randomUUID().toString();

        sesiones.put(token, usuario);

        return token;
    }

    // Metodo para obtener un usuario mediante su sesion
    public Usuario obtenerUsuario(String token) {

        if (token == null || token.isEmpty()) {
            return null;
        }

        return sesiones.get(token);
    }

    // Metodo para cerrar una sesion
    public void cerrarSesion(String token) {

        if (token != null) {
            sesiones.remove(token);
        }
    }
}
