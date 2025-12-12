package repositotio.memoria;

import dominio.Movimiento;
import repositotio.MovimientoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovimientoRepositoryInMemory implements MovimientoRepository {
    private final List<Movimiento> movimientos = new ArrayList<>();
    private long secuenciaId = 1L; // Genera IDs automáticamente

    @Override
    public Movimiento guardar(Movimiento movimiento) {
        // Asignar el ID si no tiene uno.
        if (movimiento.getId() == null) {
            movimiento.setId(secuenciaId++);
        }
        movimientos.add(movimiento);
        return  movimiento;
    }

    @Override
    public List<Movimiento> obtenerTodos() {
        return movimientos;
    }

    @Override
    public Optional<Movimiento> buscarPorId(Long id) {
        // Filtrar por id movimientos si es igual al id que buscamos
        return movimientos.stream().filter(m -> Objects.equals(m.getId(), id)).findFirst();
    }

    @Override
    public List<Movimiento> buscarPorRangoFecha(LocalDate inicio, LocalDate fin) {
        // Filtra por fechas dentro del rango (incluido)
        return movimientos.stream()
                .filter(m -> !m.getFecha().isBefore(inicio)
                        && !m.getFecha().isAfter(fin))
                .collect(Collectors.toList());
    }
}
