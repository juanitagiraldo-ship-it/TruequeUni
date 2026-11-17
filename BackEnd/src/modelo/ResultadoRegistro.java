
package modelo;

/**
 * Representa el resultado de una operacion de registro de usuario.
 *
 * @author TruequeUni
 * @version 1.0
 */
public class ResultadoRegistro {

    /**
     * Define los posibles estados de una operacion de registro.
     */
    public enum Estado {
        /** El registro del usuario fue exitoso. */
        EXITOSO,

        /** Los datos proporcionados no son validos. */
        DATOS_INVALIDOS,

        /** Ocurrio un error inesperado durante el registro. */
        ERROR
    }

    /** Estado obtenido al procesar el registro. */
    private Estado estado;

    /** Mensaje descriptivo del resultado. */
    private String mensaje;

    /**
     * Crea un resultado de registro.
     *
     * @param estado estado obtenido al procesar el registro
     * @param mensaje mensaje descriptivo del resultado
     */
    public ResultadoRegistro(Estado estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    /**
     * Obtiene el estado del registro.
     *
     * @return estado obtenido
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * Obtiene el mensaje descriptivo del resultado.
     *
     * @return mensaje del registro
     */
    public String getMensaje() {
        return mensaje;
    }
}
