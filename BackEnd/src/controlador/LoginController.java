
package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import modelo.ResultadoLogin;
import modelo.ResultadoConsultaUsuario;

public class LoginController {

    private UsuarioDAO usuarioDAO;

    public LoginController() {
        usuarioDAO = new UsuarioDAO();
    }

    public ResultadoLogin iniciarSesion(String nombreAlias, String contrasena) {

        if (nombreAlias == null || nombreAlias.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {

            return new ResultadoLogin(ResultadoLogin.Estado.DATOS_INVALIDOS, "Por favor completa todos los campos.", null);
        }

        ResultadoConsultaUsuario resultadoConsulta = usuarioDAO.obtenerPorCredenciales(nombreAlias.trim(), contrasena);

        if (resultadoConsulta.getEstado() == ResultadoConsultaUsuario.Estado.ENCONTRADO) {

            Usuario usuario = resultadoConsulta.getUsuario();

            return new ResultadoLogin(
                ResultadoLogin.Estado.EXITOSO,
                "Inicio de sesión exitoso.",
                usuario
            );

        } else if (resultadoConsulta.getEstado()
                == ResultadoConsultaUsuario.Estado.NO_ENCONTRADO) {

            return new ResultadoLogin(
                ResultadoLogin.Estado.CREDENCIALES_INVALIDAS,
                "El usuario o la contraseña no coinciden.",
                null
            );

        } else {

            return new ResultadoLogin(
                ResultadoLogin.Estado.ERROR,
                "Ocurrió un error al consultar la base de datos.",
                null
            );
        }
    }
}
