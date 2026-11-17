package modelo;

import java.util.Date;

/**
 * Representa una publicacion creada por un usuario dentro de TruequeUni.
 *
 * @author TruequeUni
 * @version 1.0
 */
public class Publicacion {

    /** Identificador unico de la publicacion. */
    private int idPublicacion;

    /** Titulo de la publicacion. */
    private String titulo;

    /** Descripcion detallada del objeto ofrecido. */
    private String descripcion;

    /** Estado fisico del objeto. */
    private String estadoFisico;

    /** Estado actual de la publicacion. */
    private String estadoPublicacion;

    /** Tipo de oferta definida por el usuario. */
    private String tipoOferta;

    /** Ruta o nombre de la imagen asociada. */
    private String imagen;

    /** Fecha en que se registro la publicacion. */
    private Date fechaPublicacion;

    /** Usuario que creo la publicacion. */
    private Usuario usuario;

    /** Categoria a la que pertenece la publicacion. */
    private Categoria categoria;

    /**
     * Crea una publicacion con todos sus datos.
     *
     * @param idPublicacion identificador unico
     * @param titulo titulo de la publicacion
     * @param descripcion descripcion del objeto
     * @param estadoFisico estado fisico del objeto
     * @param estadoPublicacion estado de la publicacion
     * @param tipoOferta tipo de oferta presentada
     * @param imagen ruta o nombre de la imagen
     * @param fechaPublicacion fecha de registro
     * @param usuario usuario propietario
     * @param categoria categoria asignada
     */
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


    /**
     * Obtiene el identificador de la publicacion.
     *
     * @return identificador de la publicacion
     */
    public int getIdPublicacion() {
        return idPublicacion;
    }

    /**
     * Establece el identificador de la publicacion.
     *
     * @param idPublicacion nuevo identificador
     */
    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    /**
     * Obtiene el titulo de la publicacion.
     *
     * @return titulo de la publicacion
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el titulo de la publicacion.
     *
     * @param titulo nuevo titulo
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene la descripcion del objeto ofrecido.
     *
     * @return descripcion de la publicacion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripcion del objeto ofrecido.
     *
     * @param descripcion nueva descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el estado fisico del objeto.
     *
     * @return estado fisico del objeto
     */
    public String getEstadoFisico() {
        return estadoFisico;
    }

    /**
     * Establece el estado fisico del objeto.
     *
     * @param estadoFisico nuevo estado fisico
     */
    public void setEstadoFisico(String estadoFisico) {
        this.estadoFisico = estadoFisico;
    }

    /**
     * Obtiene el estado actual de la publicacion.
     *
     * @return estado de la publicacion
     */
    public String getEstadoPublicacion() {
        return estadoPublicacion;
    }

    /**
     * Establece el estado actual de la publicacion.
     *
     * @param estadoPublicacion nuevo estado
     */
    public void setEstadoPublicacion(String estadoPublicacion) {
        this.estadoPublicacion = estadoPublicacion;
    }

    /**
     * Obtiene el tipo de oferta de la publicacion.
     *
     * @return tipo de oferta
     */
    public String getTipoOferta() {
        return tipoOferta;
    }

    /**
     * Establece el tipo de oferta de la publicacion.
     *
     * @param tipoOferta nuevo tipo de oferta
     */
    public void setTipoOferta(String tipoOferta) {
        this.tipoOferta = tipoOferta;
    }

    /**
     * Obtiene la ruta o nombre de la imagen asociada.
     *
     * @return imagen de la publicacion
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece la ruta o nombre de la imagen asociada.
     *
     * @param imagen nueva imagen
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Obtiene la fecha de registro de la publicacion.
     *
     * @return fecha de publicacion
     */
    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Establece la fecha de registro de la publicacion.
     *
     * @param fechaPublicacion nueva fecha
     */
    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * Obtiene el usuario propietario de la publicacion.
     *
     * @return usuario asociado
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario propietario de la publicacion.
     *
     * @param usuario nuevo usuario asociado
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene la categoria de la publicacion.
     *
     * @return categoria asociada
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoria de la publicacion.
     *
     * @param categoria nueva categoria asociada
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
}
