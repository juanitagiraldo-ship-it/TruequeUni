package controlador;

import dao.UsuarioDAO;
import modelo.ResultadoConsultaUsuario;
import modelo.ResultadoPerfil;
import modelo.Usuario;

import java.util.Arrays;
import java.util.List;

/**
 * Coordina la consulta y actualización del perfil del usuario autenticado.
 */
public class PerfilController {

    private static final List<String> PROGRAMAS = Arrays.asList(
            "Administración de Empresas",
            "Ciencia Política, Gobierno y Relaciones Internacionales",
            "Economía",
            "Negocios Internacionales",
            "Diseño Industrial",
            "Diseño de Modas",
            "Ingeniería Biomédica",
            "Ingeniería Electrónica",
            "Ingeniería Industrial",
            "Ingeniería Mecánica",
            "Ingeniería de Sistemas",
            "Fisioterapia",
            "Odontología",
            "Artes Culinarias y Gastronomía"
    );

    private final UsuarioDAO usuarioDAO;

    public PerfilController() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Obtiene los datos actuales del usuario.
     */
    public ResultadoPerfil consultarPerfil(int idUsuario) {
        if (idUsuario <= 0) {
            return new ResultadoPerfil(
                    ResultadoPerfil.Estado.DATOS_INVALIDOS,
                    "El usuario no es válido.",
                    null);
        }

        ResultadoConsultaUsuario resultado = usuarioDAO.obtenerPorId(idUsuario);
        if (resultado.getEstado() == ResultadoConsultaUsuario.Estado.ENCONTRADO) {
            return new ResultadoPerfil(
                    ResultadoPerfil.Estado.EXITOSO,
                    "Perfil consultado correctamente.",
                    resultado.getUsuario());
        }
        if (resultado.getEstado() == ResultadoConsultaUsuario.Estado.NO_ENCONTRADO) {
            return new ResultadoPerfil(
                    ResultadoPerfil.Estado.NO_ENCONTRADO,
                    "No se encontró el perfil.",
                    null);
        }
        return new ResultadoPerfil(
                ResultadoPerfil.Estado.ERROR,
                "No se pudo consultar el perfil.",
                null);
    }

    /**
     * Actualiza únicamente los campos editables del usuario.
     */
    public ResultadoPerfil actualizarPerfil(
            Usuario usuarioAutenticado,
            String nombreAlias,
            String programa,
            String rol,
            String nuevaContrasena) {

        String nombre = nombreAlias == null ? "" : nombreAlias.trim();
        String programaSeleccionado = programa == null ? "" : programa.trim();
        String contrasena = nuevaContrasena == null ? "" : nuevaContrasena;

        if (usuarioAutenticado == null
                || usuarioAutenticado.getIdUsuario() <= 0
                || nombre.isEmpty()
                || nombre.length() > 100
                || !PROGRAMAS.contains(programaSeleccionado)
                || !("estudiante".equals(rol) || "docente".equals(rol))
                || contrasena.length() > 255) {
            return new ResultadoPerfil(
                    ResultadoPerfil.Estado.DATOS_INVALIDOS,
                    "Verifica los datos ingresados.",
                    null);
        }

        int resultado = usuarioDAO.actualizarPerfil(
                usuarioAutenticado.getIdUsuario(),
                nombre,
                programaSeleccionado,
                rol,
                contrasena);

        if (resultado < 0) {
            return new ResultadoPerfil(
                    ResultadoPerfil.Estado.ERROR,
                    "No se pudo actualizar el perfil.",
                    null);
        }
        if (resultado == 0) {
            return new ResultadoPerfil(
                    ResultadoPerfil.Estado.NO_ENCONTRADO,
                    "No se encontró el perfil.",
                    null);
        }

        Usuario usuarioActualizado = new Usuario(
                usuarioAutenticado.getIdUsuario(),
                nombre,
                "",
                programaSeleccionado,
                rol,
                usuarioAutenticado.getReputacion());
        return new ResultadoPerfil(
                ResultadoPerfil.Estado.EXITOSO,
                "Perfil actualizado correctamente.",
                usuarioActualizado);
    }
}
