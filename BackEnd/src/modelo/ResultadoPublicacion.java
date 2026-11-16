package modelo;

public class ResultadoPublicacion {

    public enum Estado {
        EXITOSO,
        DATOS_INVALIDOS,
        NO_ENCONTRADO,
        ERROR
    }

    private final Estado estado;
    private final String mensaje;

    public ResultadoPublicacion(Estado estado, String mensaje) {
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
