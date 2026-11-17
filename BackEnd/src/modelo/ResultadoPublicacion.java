package modelo;

/**
 * Representa el resultado de una operacion realizada sobre una publicacion.
 *
 * @author TruequeUni
 * @version 1.0
 */
public class ResultadoPublicacion {

    /**
     * Define los posibles estados de una operacion sobre publicaciones.
     */
    public enum Estado {
        /** La operacion sobre la publicacion fue exitosa. */
        EXITOSO,

        /** Los datos enviados no cumplen los requisitos establecidos. */
        DATOS_INVALIDOS,

        /** No se encontro la publicacion solicitada. */
        NO_ENCONTRADO,

        /** Ocurrio un error inesperado durante la operacion. */
        ERROR
    }

    /** Estado obtenido al procesar la operacion. */
    private final Estado estado;

    /** Mensaje descriptivo del resultado. */
    private final String mensaje;

    /**
     * Crea un resultado de una operacion sobre publicaciones.
     *
     * @param estado estado obtenido al procesar la operacion
     * @param mensaje mensaje descriptivo del resultado
     */
    public ResultadoPublicacion(Estado estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    /**
     * Obtiene el estado de la operacion.
     *
     * @return estado obtenido
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * Obtiene el mensaje descriptivo del resultado.
     *
     * @return mensaje de la operacion
     */
    public String getMensaje() {
        return mensaje;
    }
}
