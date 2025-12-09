package servicio;

import dominio.CategoriaGasto;
import dominio.Movimiento;
import dominio.PresupuestoMensual;
import dominio.ResumenMensual;
import repositotio.CategoriaRepository;
import repositotio.MovimientoRepository;
import repositotio.PresupuestoRepository;
import utils.FechaUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FinanzasService {
    private final CategoriaRepository categoriaRepository;
    private final MovimientoRepository movimientoRepository;
    private final PresupuestoRepository presupuestoRepository;

    // Inyección de dependencias: permite intercambiar repositorios fácilmente
    public FinanzasService(CategoriaRepository c, MovimientoRepository m, PresupuestoRepository p) {
        this.categoriaRepository = c;
        this.movimientoRepository = m;
        this.presupuestoRepository = p;
    }

    public List<CategoriaGasto> obtenerCategorias() {
        return categoriaRepository.obtenerTodas();
    }

    // Registrar categoría personalizada
    public CategoriaGasto registrarCategoria(String nombre, String tipo) {
        CategoriaGasto categoriaGasto = new CategoriaGasto(null, nombre, tipo);
        return  categoriaRepository.guardar(categoriaGasto);

    }

    // Registrar un ingreso o gasto
    public void registrarMovimiento(String tipo, double monto, LocalDate fecha,
                                    Long categoriaId, String descripcion) {

        // Valida y obtiene la categoría existente
        CategoriaGasto categoria = categoriaRepository.buscarPorId(categoriaId)
                .orElseThrow(() -> new RuntimeException("La categoría no existe"));
        movimientoRepository.guardar(new Movimiento(null, tipo, monto, fecha, categoria, descripcion));
    }

    public void configurarPresupuesto(int anio, int mes, double monto) {
        presupuestoRepository.guardar(new PresupuestoMensual(anio, mes, monto));
    }

    public List<Movimiento> listarMovimientos(int anio, int mes) {
        return movimientoRepository.buscarPorRangoFecha(
                FechaUtils.inicioDeMes(anio, mes),
                FechaUtils.finDeMes(anio, mes)
        );
    }

    // Calcula el resumen mensual completo
    public ResumenMensual obtenerResumen(int anio, int mes) {
        List<Movimiento> lista = listarMovimientos(anio, mes);

        double ingresos = lista.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase("INGRESO"))
                .mapToDouble(Movimiento::getMonto)
                .sum();

        double gastos = lista.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase("GASTO"))
                .mapToDouble(Movimiento::getMonto)
                .sum();

        double saldo = ingresos - gastos;

        // Agrupación de gastos por categoría
        Map<String, Double> gastosPorCategoria = lista.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase("GASTO"))
                .collect(Collectors.groupingBy(
                        m -> m.getCategoria().getNombre(),
                        Collectors.summingDouble(Movimiento::getMonto)
                ));

        // Obtiene presupuesto si existe
        Double presupuesto = presupuestoRepository.buscarPorAnioYMes(anio, mes)
                .map(PresupuestoMensual::getMontoTotal)
                .orElse(null);

        return new ResumenMensual(anio, mes, ingresos, gastos, saldo, presupuesto, gastosPorCategoria);
    }
}
