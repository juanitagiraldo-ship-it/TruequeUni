
package modelo;

/**
 * Representa el resultado obtenido al consultar un usuario.
 *
 * @author TruequeUni
 * @version 1.0
 */
public class ResultadoConsultaUsuario {

    /**
     * Define los posibles estados de una consulta de usuario.
     */
    public enum Estado {
        /** El usuario fue encontrado. */
        ENCONTRADO,

        /** No existe un usuario asociado a los datos consultados. */
        NO_ENCONTRADO,

        /** Ocurrio un error durante la consulta. */
        ERROR
    }

    /**
     * Estado final de la consulta.
     */
    private Estado estado;

    /**
     * Usuario encontrado durante la consulta.
     */
    private Usuario usuario;

    /**
     * Crea un resultado de consulta con su estado y usuario asociado.
     *
     * @param estado estado obtenido al realizar la consulta
     * @param usuario usuario encontrado, o {@code null} si no existe
     */
    public ResultadoConsultaUsuario(Estado estado, Usuario usuario) {
        this.estado = estado;
        this.usuario = usuario;
    }

    /**
     * Obtiene el estado de la consulta.
     *
     * @return estado obtenido al consultar el usuario
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * Obtiene el usuario encontrado durante la consulta.
     *
     * @return usuario encontrado, o {@code null} si no fue posible obtenerlo
     */
    public Usuario getUsuario() {
        return usuario;
    }
}
