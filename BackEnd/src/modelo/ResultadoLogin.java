
package modelo;

public class ResultadoLogin {

    private boolean exitoso;
    private String mensaje;
    private Usuario usuario;

    public ResultadoLogin(boolean exitoso, String mensaje, Usuario usuario) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.usuario = usuario;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
