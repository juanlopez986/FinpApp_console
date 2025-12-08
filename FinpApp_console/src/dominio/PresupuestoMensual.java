package dominio;

public class PresupuestoMensual {
    private int anio;
    private int mes;
    private double montoTotal;

    public  PresupuestoMensual(int anio, int mes, double montoTotal) {
        this.anio = anio;
        this.mes = mes;
        this.montoTotal = montoTotal;
    }

    public int getAnio() { return anio; }
    public int getMes() { return mes; }
    public double getMontoTotal() { return montoTotal; }
}
