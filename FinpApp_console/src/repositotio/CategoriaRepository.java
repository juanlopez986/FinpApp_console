package repositotio;

import dominio.CategoriaGasto;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {
    CategoriaGasto guardar(CategoriaGasto categoria);
    List<CategoriaGasto> obtenerTodas();
    Optional<CategoriaGasto> buscarPorId(Long id);

}
