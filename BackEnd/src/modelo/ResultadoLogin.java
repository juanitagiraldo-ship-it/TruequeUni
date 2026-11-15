
package modelo;

public class ResultadoLogin {

    public enum Estado {
        EXITOSO,
        CREDENCIALES_INVALIDAS,
        DATOS_INVALIDOS,
        ERROR
    }

    private Estado estado;
    private String mensaje;
    private Usuario usuario;

    public ResultadoLogin(Estado estado, String mensaje, Usuario usuario) {
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
