package modelo;
import java.util.Date;

public class Trueque {


    private int idTrueque;
    private Date fechaSolicitud;
    private String estado;
    private String tipoSolicitud;
    private Usuario usuario;
    private Publicacion publicacion;

    
    public Trueque(int idTrueque, Date fechaSolicitud, String estado,
            String tipoSolicitud, Usuario usuario, Publicacion publicacion) {

        this.idTrueque = idTrueque;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.tipoSolicitud = tipoSolicitud;
        this.usuario = usuario;
        this.publicacion = publicacion;
    }

    
    public int getIdTrueque() {
        return idTrueque;
    }

    public void setIdTrueque(int idTrueque) {
        this.idTrueque = idTrueque;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }
}
