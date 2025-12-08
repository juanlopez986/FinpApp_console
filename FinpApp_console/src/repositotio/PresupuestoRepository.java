package repositotio;

import dominio.PresupuestoMensual;

import java.util.Optional;

public interface PresupuestoRepository {
    PresupuestoMensual guardar(PresupuestoMensual p);
    Optional<PresupuestoMensual> buscarPorAnioYMes(int anio, int mes);
}
