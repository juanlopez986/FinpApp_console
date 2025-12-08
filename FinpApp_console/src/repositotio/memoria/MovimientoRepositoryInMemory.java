package repositotio.memoria;

import dominio.Movimiento;
import repositotio.MovimientoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovimientoRepositoryInMemory implements MovimientoRepository {
    private final List<Movimiento> movimientos = new ArrayList<>();
    private long secuenciaId = 1L;

    @Override
    public Movimiento guardar(Movimiento movimiento) {

        // Si viene sin id, se crea una copia con un ID nuevo
        if (movimiento.getId() == null) {
            movimiento = new Movimiento(secuenciaId++,
                    movimiento.getTipo(),
                    movimiento.getMonto(),
                    movimiento.getFecha(),
                    movimiento.getCategoria(),
                    movimiento.getDescripcion()
            );
        }
        movimientos.add(movimiento);
        return  movimiento;
    }

    @Override
    public List<Movimiento> obtenerTodos() {
        return movimientos;
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
