package dominio;

public class CategoriaGasto {
    private Long id;
    private String nombre;
    private String tipo; // FIJO / VARIABLE
    private String descripcion;
    public CategoriaGasto(Long id, String nombre, String tipo, String descripcion){
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return  descripcion; }

    // Setters
    public void setId(Long id) { this.id = id; }

    @Override
    public String toString() {
        // Representación para mostrarla en consola.
        return id + " | " + nombre + " | " + " " + tipo + " " + " | " + descripcion;
    }
}

