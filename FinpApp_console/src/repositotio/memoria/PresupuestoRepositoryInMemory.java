package repositotio.memoria;

import dominio.PresupuestoMensual;
import repositotio.PresupuestoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PresupuestoRepositoryInMemory implements PresupuestoRepository {
    private final List<PresupuestoMensual> presupuestoMensuals = new ArrayList<>();

    @Override
    public PresupuestoMensual guardar(PresupuestoMensual p) {

        // Elimina presupuesto previo del mismo mes/año
        presupuestoMensuals.removeIf(x -> x.getAnio() == p.getAnio()
                && x.getMes() == p.getMes());
        presupuestoMensuals.add(p);
        return p;
    }

    @Override
    public Optional<PresupuestoMensual> buscarPorAnioYMes(int anio, int mes) {
        return presupuestoMensuals.stream()
                .filter(p -> p.getAnio() == anio && p.getMes() == mes)
                .findFirst();
    }
}
