package modelo;

public class ResultadoPerfil {

    public enum Estado {
        EXITOSO,
        DATOS_INVALIDOS,
        NO_ENCONTRADO,
        ERROR
    }

    private final Estado estado;
    private final String mensaje;
    private final Usuario usuario;

    public ResultadoPerfil(Estado estado, String mensaje, Usuario usuario) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.usuario = usuario;
    }

    public Estado getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
