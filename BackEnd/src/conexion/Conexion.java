package conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexion con la base de datos MySQL de TruequeUni.
 *
 * @author TruequeUni
 * @version 1.0
 */
public class Conexion {

    /**
     * URL JDBC de la base de datos.
     */
    private static final String URL = "jdbc:mysql://localhost:3306/trueque_uni";

    /**
     * Nombre de usuario utilizado para conectar con MySQL.
     */
    private static final String USUARIO = "root";

    /**
     * Establece una conexion con la base de datos MySQL.
     * <p>
     * La clave de acceso se obtiene de la variable de entorno
     * {@code MYSQL_PASSWORD}. Si la variable no esta configurada o esta vacia,
     * no se intenta establecer la conexion.
     * </p>
     *
     * @return una conexion activa con MySQL, o {@code null} si no fue posible
     *         establecerla
     */
    public static Connection conectar() {

        Connection conexion = null;
        String contrasena = System.getenv("MYSQL_PASSWORD");

        try {
            if (contrasena == null || contrasena.isEmpty()) {
                throw new SQLException("No se ha configurado la contraseña de MySQL.");
            }

            conexion = DriverManager.getConnection(URL, USUARIO, contrasena);

            System.out.println("Conexion exitosa con MySQL");

        } catch (SQLException e) {
            System.out.println("Error al conectar con MySQL: " + e.getMessage());
        }

        return conexion;
    }
}

