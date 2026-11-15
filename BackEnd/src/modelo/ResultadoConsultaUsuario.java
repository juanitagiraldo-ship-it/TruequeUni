
package modelo;

public class ResultadoConsultaUsuario {

    public enum Estado {
        ENCONTRADO,
        NO_ENCONTRADO,
        ERROR
    }

    private Estado estado;
    private Usuario usuario;

    public ResultadoConsultaUsuario(Estado estado, Usuario usuario) {
        this.estado = estado;
        this.usuario = usuario;
    }

    public Estado getEstado() {
        return estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
