package conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/trueque_uni";
    private static final String USUARIO = "root";

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

