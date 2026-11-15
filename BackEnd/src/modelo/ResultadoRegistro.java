
package modelo;

public class ResultadoRegistro {

    public enum Estado {
        EXITOSO,
        DATOS_INVALIDOS,
        ERROR
    }

    private Estado estado;
    private String mensaje;

    public ResultadoRegistro(Estado estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    public Estado getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }
}
