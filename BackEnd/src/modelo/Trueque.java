package modelo;
import java.util.Date;

/**
 * Representa una solicitud de intercambio entre un usuario y una publicacion.
 *
 * @author TruequeUni
 * @version 1.0
 */
public class Trueque {

    /** Identificador unico del trueque. */
    private int idTrueque;

    /** Fecha en que se realizo la solicitud. */
    private Date fechaSolicitud;

    /** Estado actual de la solicitud. */
    private String estado;

    /** Tipo de solicitud presentada por el usuario. */
    private String tipoSolicitud;

    /** Usuario que realiza la solicitud. */
    private Usuario usuario;

    /** Publicacion asociada al intercambio. */
    private Publicacion publicacion;

    /**
     * Crea un trueque con sus datos principales.
     *
     * @param idTrueque identificador unico del trueque
     * @param fechaSolicitud fecha de la solicitud
     * @param estado estado actual del trueque
     * @param tipoSolicitud tipo de solicitud realizada
     * @param usuario usuario solicitante
     * @param publicacion publicacion relacionada
     */
    public Trueque(int idTrueque, Date fechaSolicitud, String estado,
            String tipoSolicitud, Usuario usuario, Publicacion publicacion) {

        this.idTrueque = idTrueque;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.tipoSolicitud = tipoSolicitud;
        this.usuario = usuario;
        this.publicacion = publicacion;
    }

    /**
     * Obtiene el identificador del trueque.
     *
     * @return identificador del trueque
     */
    public int getIdTrueque() {
        return idTrueque;
    }

    /**
     * Establece el identificador del trueque.
     *
     * @param idTrueque nuevo identificador
     */
    public void setIdTrueque(int idTrueque) {
        this.idTrueque = idTrueque;
    }

    /**
     * Obtiene la fecha de la solicitud.
     *
     * @return fecha en que se solicito el trueque
     */
    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    /**
     * Establece la fecha de la solicitud.
     *
     * @param fechaSolicitud nueva fecha de solicitud
     */
    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    /**
     * Obtiene el estado actual del trueque.
     *
     * @return estado del trueque
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado actual del trueque.
     *
     * @param estado nuevo estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el tipo de solicitud del trueque.
     *
     * @return tipo de solicitud
     */
    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    /**
     * Establece el tipo de solicitud del trueque.
     *
     * @param tipoSolicitud nuevo tipo de solicitud
     */
    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    /**
     * Obtiene el usuario que solicito el trueque.
     *
     * @return usuario solicitante
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario que solicito el trueque.
     *
     * @param usuario nuevo usuario solicitante
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene la publicacion relacionada con el trueque.
     *
     * @return publicacion asociada
     */
    public Publicacion getPublicacion() {
        return publicacion;
    }

    /**
     * Establece la publicacion relacionada con el trueque.
     *
     * @param publicacion nueva publicacion asociada
     */
    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }
}
