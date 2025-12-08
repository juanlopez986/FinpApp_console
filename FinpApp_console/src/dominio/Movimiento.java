package dominio;

import java.time.LocalDate;

public class Movimiento {
    private Long id;
    private String tipo; // INGRESO / GASTO
    private double monto;
    private LocalDate fecha;
    private CategoriaGasto categoria;
    private String descripcion;

    public Movimiento(Long id, String tipo, double monto, LocalDate fecha,
                      CategoriaGasto categoria, String descripcion){
        this.id = id;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    // Getters
    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public double getMonto() { return monto; }
    public LocalDate getFecha() { return fecha; }
    public CategoriaGasto getCategoria() { return categoria; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() {
        // Representación para mostrar el movimiento en consola.
        return id + " | " + tipo + " | $" + monto + fecha +
                " | " + categoria.getNombre() + " | " + descripcion;
    }
}
