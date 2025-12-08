package dominio;

import java.util.Map;

public class ResumenMensual {
    public int anio;
    public int mes;
    public double totalIngresos;
    public double totalGastos;
    public double saldo;
    public Double presupuestoMensual; // Puede ser null si no existe presupuesto.
    public Map<String, Double> gastosPorCategoria;

    public ResumenMensual(int anio, int mes, double totalIngresos,
                          double totalGastos, double saldo, Double presupuestoMensual,
                          Map<String, Double> gastosPorCategoria) {
        this.anio = anio;
        this.mes = mes;
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
        this.saldo = saldo;
        this.presupuestoMensual = presupuestoMensual;
        this.gastosPorCategoria = gastosPorCategoria;
    }
}
