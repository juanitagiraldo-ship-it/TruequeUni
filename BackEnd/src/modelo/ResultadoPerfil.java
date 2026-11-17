package modelo;

/**
 * Representa el resultado de consultar o actualizar el perfil de un usuario.
 *
 * @author TruequeUni
 * @version 1.0
 */
public class ResultadoPerfil {

    /**
     * Define los posibles estados de una operacion sobre el perfil.
     */
    public enum Estado {
        /** La consulta o actualizacion del perfil fue exitosa. */
        EXITOSO,

        /** Los datos enviados no cumplen los requisitos establecidos. */
        DATOS_INVALIDOS,

        /** No se encontro el perfil solicitado. */
        NO_ENCONTRADO,

        /** Ocurrio un error inesperado durante la operacion. */
        ERROR
    }

    /** Estado obtenido al procesar la operacion. */
    private final Estado estado;

    /** Mensaje descriptivo del resultado. */
    private final String mensaje;

    /** Usuario asociado al perfil consultado o actualizado. */
    private final Usuario usuario;

    /**
     * Crea un resultado de una operacion de perfil.
     *
     * @param estado estado obtenido al procesar la operacion
     * @param mensaje mensaje descriptivo del resultado
     * @param usuario usuario asociado al perfil, o {@code null} si no aplica
     */
    public ResultadoPerfil(Estado estado, String mensaje, Usuario usuario) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.usuario = usuario;
    }

    /**
     * Obtiene el estado de la operacion de perfil.
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

    /**
     * Obtiene el usuario asociado al perfil.
     *
     * @return usuario consultado o actualizado, o {@code null} si no esta disponible
     */
    public Usuario getUsuario() {
        return usuario;
    }
}
