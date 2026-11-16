
package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import modelo.ResultadoLogin;
import modelo.ResultadoConsultaUsuario;

/**
 * Coordina la autenticación de usuarios mediante sus credenciales.
 */
public class LoginController {

    private UsuarioDAO usuarioDAO;

    /**
     * Crea el controlador e inicializa el acceso a los datos de usuarios.
     */
    public LoginController() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Valida las credenciales e intenta iniciar una sesión.
     *
     * @param nombreAlias alias del usuario
     * @param contrasena contraseña del usuario
     * @return resultado de la autenticación, con su estado y, si corresponde,
     *         los datos del usuario autenticado
     */
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
