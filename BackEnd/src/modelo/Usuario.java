package modelo;

/**
 * Representa a un usuario registrado en la plataforma TruequeUni.
 *
 * @author TruequeUni
 * @version 1.0
 */
public class Usuario {

    /** Identificador unico del usuario. */
    private int idUsuario;

    /** Nombre o alias visible del usuario. */
    private String nombreAlias;

    /** Contrasena asociada a la cuenta del usuario. */
    private String contrasena;

    /** Programa academico al que pertenece el usuario. */
    private String programa;

    /** Rol del usuario dentro de la plataforma. */
    private String rol;

    /** Reputacion acumulada del usuario. */
    private double reputacion;

    /**
     * Crea un usuario con sus datos principales.
     *
     * @param idUsuario identificador unico del usuario
     * @param nombreAlias nombre o alias visible
     * @param contrasena contrasena de acceso
     * @param programa programa academico
     * @param rol rol dentro de la plataforma
     * @param reputacion reputacion acumulada
     */
    public Usuario(int idUsuario, String nombreAlias, String contrasena,
                   String programa, String rol, double reputacion) {
        this.idUsuario = idUsuario;
        this.nombreAlias = nombreAlias;
        this.contrasena = contrasena;
        this.programa = programa;
        this.rol = rol;
        this.reputacion = reputacion;
    }

    /**
     * Obtiene el identificador del usuario.
     *
     * @return identificador del usuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el identificador del usuario.
     *
     * @param idUsuario nuevo identificador
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el nombre o alias del usuario.
     *
     * @return nombre o alias visible
     */
    public String getNombreAlias() {
        return nombreAlias;
    }

    /**
     * Establece el nombre o alias del usuario.
     *
     * @param nombreAlias nuevo nombre o alias
     */
    public void setNombreAlias(String nombreAlias) {
        this.nombreAlias = nombreAlias;
    }

    /**
     * Obtiene la contrasena del usuario.
     *
     * @return contrasena almacenada
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contrasena del usuario.
     *
     * @param contrasena nueva contrasena
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el programa academico del usuario.
     *
     * @return programa academico
     */
    public String getPrograma() {
        return programa;
    }

    /**
     * Establece el programa academico del usuario.
     *
     * @param programa nuevo programa academico
     */
    public void setPrograma(String programa) {
        this.programa = programa;
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return rol dentro de la plataforma
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param rol nuevo rol
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Obtiene la reputacion acumulada del usuario.
     *
     * @return reputacion del usuario
     */
    public double getReputacion() {
        return reputacion;
    }

    /**
     * Establece la reputacion acumulada del usuario.
     *
     * @param reputacion nueva reputacion
     */
    public void setReputacion(double reputacion) {
        this.reputacion = reputacion;
    }
}