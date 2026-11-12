package modelo;

public class Usuario {

   
    private int idUsuario;
    private String nombreAlias;
    private String contrasena;
    private String programa;
    private String rol;
    private double reputacion;

    public Usuario(int idUsuario, String nombreAlias, String contrasena,
                   String programa, String rol, double reputacion) {
        this.idUsuario = idUsuario;
        this.nombreAlias = nombreAlias;
        this.contrasena = contrasena;
        this.programa = programa;
        this.rol = rol;
        this.reputacion = reputacion;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreAlias() {
        return nombreAlias;
    }

    public void setNombreAlias(String nombreAlias) {
        this.nombreAlias = nombreAlias;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public double getReputacion() {
        return reputacion;
    }

    public void setReputacion(double reputacion) {
        this.reputacion = reputacion;
    }
}