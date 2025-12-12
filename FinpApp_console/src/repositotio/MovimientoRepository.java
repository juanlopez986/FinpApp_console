package repositotio;

import dominio.Movimiento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepository {
    Movimiento guardar(Movimiento m);
    List<Movimiento> obtenerTodos();
    Optional<Movimiento> buscarPorId(Long id);
    List<Movimiento> buscarPorRangoFecha(LocalDate inicio, LocalDate fin);
}
