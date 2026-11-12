public class PruebaConexion {

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("Controlador MySQL encontrado");

        } catch (ClassNotFoundException e) {

            System.out.println("No se encontro el controlador MySQL");
        }

        Conexion.conectar();
    }
}