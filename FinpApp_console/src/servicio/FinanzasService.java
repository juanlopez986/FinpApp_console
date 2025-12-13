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

    // Registrar la categoría
    public CategoriaGasto registrarCategoria(String nombre, String tipo, String descripcion) {
        CategoriaGasto categoriaGasto = new CategoriaGasto(null, nombre, tipo.toUpperCase(), descripcion);

        return categoriaRepository.guardar(categoriaGasto);
    }

    // Listar todas las categorías
    public List<CategoriaGasto> listarCategorias() {
        return categoriaRepository.obtenerTodas();
    }

    // Registrar un ingreso o gasto
    public Movimiento registrarMovimiento(String tipo, double monto, LocalDate fecha,
                                    Long categoriaId, String descripcion) {

        // Validar y obtiener la categoría existente
        CategoriaGasto categoria = categoriaRepository.buscarPorId(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: id=" + categoriaId));

        Movimiento movimiento = new Movimiento(null, tipo.toUpperCase(), monto, fecha, categoria, descripcion);
        return movimientoRepository.guardar(movimiento);
    }

    // Obtner los movimientos realizados por año y mes
    public List<Movimiento> obtenerMovimientosMes(int anio, int mes) {
        return movimientoRepository.buscarPorRangoFecha(
                FechaUtils.inicioDeMes(anio, mes),
                FechaUtils.finDeMes(anio, mes)
        );
    }

    // Configurar el presupuesto por año y mes
    public PresupuestoMensual configurarPresupuesto(int anio, int mes, double monto) {
        PresupuestoMensual presupuestoMensual = new PresupuestoMensual(anio, mes, monto);
        return presupuestoRepository.guardar(presupuestoMensual);
    }

    // Calcular el resumen mensual de todos los movimientos
    public ResumenMensual obtenerResumen(int anio, int mes) {
        List<Movimiento> movimientos = obtenerMovimientosMes(anio, mes);

        double ingresos = movimientos.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase("INGRESO"))
                .mapToDouble(Movimiento::getMonto)
                .sum();

        double gastos = movimientos.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase("GASTO"))
                .mapToDouble(Movimiento::getMonto)
                .sum();

        double saldo;
        // Solo calcular si hay INGRESOS y GASTOS
        if (ingresos > 0 && gastos > 0) {
            saldo = ingresos - gastos;
        } else {
            // Si solo hay gastos o solo hay ingresos, devolver 0
            saldo = 0;
        }

        // Agrupar por gastos por categoría
        Map<String, Double> gastosPorCategoria = movimientos.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase("GASTO"))
                .collect(Collectors.groupingBy(
                        m -> m.getCategoria().getNombre(),
                        Collectors.summingDouble(Movimiento::getMonto)
                ));

        // Calcular el total gastos por categoría
        double totalGastosPorCategoria = gastosPorCategoria.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        // Obtiener el presupuesto si existe
        Double presupuesto = presupuestoRepository.buscarPorAnioYMes(anio, mes)
                .map(PresupuestoMensual::getMontoTotal)
                .orElse(null);

        return new ResumenMensual(anio, mes, ingresos, gastos, saldo,
                presupuesto, gastosPorCategoria, totalGastosPorCategoria);
    }
}
