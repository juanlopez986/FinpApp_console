package repositotio;

import dominio.Movimiento;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoRepository {
    Movimiento guardar(Movimiento m);
    List<Movimiento> obtenerTodos();
    List<Movimiento> buscarPorRangoFecha(LocalDate inicio, LocalDate fin);
}
