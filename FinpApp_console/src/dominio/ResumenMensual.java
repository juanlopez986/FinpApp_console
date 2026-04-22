package dominio;

import java.util.Map;

public class ResumenMensual {
    private int anio;
    private int mes;
    private double totalIngresos;
    private double totalGastos;
    private double saldo;
    private Double presupuestoMensual; // Puede ser null si no existe presupuesto.
    private Map<String, Double> gastosPorCategoria;

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

    // Getters
    public int getAnio() { return anio; }
    public int getMes() { return mes; }
    public double getTotalIngresos() { return totalIngresos; }
    public double getTotalGastos() { return totalGastos; }
    public double getSaldo() { return saldo; }
    public Double getPresupuestoMensual() { return presupuestoMensual; }
    public Map<String, Double> getGastosPorCategoria() { return gastosPorCategoria; }
}
