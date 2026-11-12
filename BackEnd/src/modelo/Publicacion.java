package modelo;

import java.util.Date;

public class Publicacion {
    

    private int idPublicacion;
    private String titulo;
    private String descripcion;
    private String estadoFisico;
    private String estadoPublicacion;
    private String tipoOferta;
    private String imagen;
    private Date fechaPublicacion;
    private Usuario usuario;
    private Categoria categoria;

   
    public Publicacion(int idPublicacion, String titulo, String descripcion,
            String estadoFisico, String estadoPublicacion, String tipoOferta,
            String imagen, Date fechaPublicacion, Usuario usuario, Categoria categoria) {

        this.idPublicacion = idPublicacion;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estadoFisico = estadoFisico;
        this.estadoPublicacion = estadoPublicacion;
        this.tipoOferta = tipoOferta;
        this.imagen = imagen;
        this.fechaPublicacion = fechaPublicacion;
        this.usuario = usuario;
        this.categoria = categoria;
    }


    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstadoFisico() {
        return estadoFisico;
    }

    public void setEstadoFisico(String estadoFisico) {
        this.estadoFisico = estadoFisico;
    }

    public String getEstadoPublicacion() {
        return estadoPublicacion;
    }

    public void setEstadoPublicacion(String estadoPublicacion) {
        this.estadoPublicacion = estadoPublicacion;
    }

    public String getTipoOferta() {
        return tipoOferta;
    }

    public void setTipoOferta(String tipoOferta) {
        this.tipoOferta = tipoOferta;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
}
