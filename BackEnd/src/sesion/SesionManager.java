
package sesion;

import modelo.Usuario;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Administra las sesiones activas de los usuarios de TruequeUni.
 * <p>
 * Cada sesion se identifica mediante un token generado aleatoriamente y se
 * almacena en memoria mientras el usuario permanece conectado.
 * </p>
 *
 * @author TruequeUni
 * @version 1.0
 */
public class SesionManager {

    /**
     * Coleccion compartida que relaciona los tokens de sesion con sus usuarios.
     */
    private static final Map<String, Usuario> sesiones =
            new ConcurrentHashMap<>();

    /**
     * Crea una nueva sesion para el usuario indicado.
     *
     * @param usuario usuario que iniciara la sesion
     * @return token unico generado para identificar la sesion
     */
    public String crearSesion(Usuario usuario) {

        String token = UUID.randomUUID().toString();

        sesiones.put(token, usuario);

        return token;
    }

    /**
     * Obtiene el usuario asociado a un token de sesion.
     *
     * @param token token de la sesion que se desea consultar
     * @return usuario asociado al token, o {@code null} si el token no es valido
     *         o no existe una sesion activa
     */
    public Usuario obtenerUsuario(String token) {

        if (token == null || token.isEmpty()) {
            return null;
        }

        return sesiones.get(token);
    }

    /**
     * Cierra una sesion eliminando su token y su usuario asociado.
     *
     * @param token token de la sesion que se desea cerrar
     */
    public void cerrarSesion(String token) {

        if (token != null) {
            sesiones.remove(token);
        }
    }
}
