
package modelo;

/**
 * Representa el resultado de una operacion de inicio de sesion.
 *
 * @author TruequeUni
 * @version 1.0
 */
public class ResultadoLogin {

    /**
     * Define los posibles estados del proceso de inicio de sesion.
     */
    public enum Estado {
        /** Las credenciales fueron validadas y el inicio fue exitoso. */
        EXITOSO,

        /** El nombre de usuario o la contrasena no son correctos. */
        CREDENCIALES_INVALIDAS,

        /** Los datos enviados no cumplen los requisitos requeridos. */
        DATOS_INVALIDOS,

        /** Ocurrio un error inesperado durante el inicio de sesion. */
        ERROR
    }

    /** Estado obtenido al procesar el inicio de sesion. */
    private Estado estado;

    /** Mensaje descriptivo del resultado. */
    private String mensaje;

    /** Usuario autenticado cuando el inicio de sesion fue exitoso. */
    private Usuario usuario;

    /**
     * Crea un resultado de inicio de sesion.
     *
     * @param estado estado obtenido al procesar el inicio de sesion
     * @param mensaje mensaje descriptivo del resultado
     * @param usuario usuario autenticado, o {@code null} si no fue exitoso
     */
    public ResultadoLogin(Estado estado, String mensaje, Usuario usuario) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.usuario = usuario;
    }

    /**
     * Obtiene el estado del inicio de sesion.
     *
     * @return estado obtenido
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * Obtiene el mensaje descriptivo del resultado.
     *
     * @return mensaje del resultado
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Obtiene el usuario autenticado.
     *
     * @return usuario autenticado, o {@code null} si el inicio no fue exitoso
     */
    public Usuario getUsuario() {
        return usuario;
    }
}
