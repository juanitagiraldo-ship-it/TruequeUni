package modelo;

/**
 * Representa una categoria disponible para clasificar las publicaciones.
 *
 * @author TruequeUni
 * @version 1.0
 */
public class Categoria {

    /**
     * Identificador unico de la categoria.
     */
    private int idCategoria;

    /**
     * Nombre de la categoria.
     */
    private String nombreCategoria;

    /**
     * Descripcion de los elementos que pertenecen a la categoria.
     */
    private String descripcion;

    /**
     * Crea una categoria con sus datos principales.
     *
     * @param idCategoria identificador unico de la categoria
     * @param nombreCategoria nombre de la categoria
     * @param descripcion descripcion de la categoria
     */
    public Categoria(int idCategoria, String nombreCategoria, String descripcion) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el identificador de la categoria.
     *
     * @return identificador de la categoria
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * Establece el identificador de la categoria.
     *
     * @param idCategoria nuevo identificador
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Obtiene el nombre de la categoria.
     *
     * @return nombre de la categoria
     */
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    /**
     * Establece el nombre de la categoria.
     *
     * @param nombreCategoria nuevo nombre
     */
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    /**
     * Obtiene la descripcion de la categoria.
     *
     * @return descripcion de la categoria
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripcion de la categoria.
     *
     * @param descripcion nueva descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

