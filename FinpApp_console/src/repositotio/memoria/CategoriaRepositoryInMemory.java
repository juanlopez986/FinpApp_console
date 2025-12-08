package repositotio.memoria;

import dominio.CategoriaGasto;
import repositotio.CategoriaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaRepositoryInMemory implements CategoriaRepository {
    private final List<CategoriaGasto> categorias = new ArrayList<>();
    private long secuenciaId = 1L;

    public CategoriaRepositoryInMemory() {

        // CATEGORÍAS BASE PRECARGADAS
        guardar(new CategoriaGasto(null, "Tecnología", "VARIABLE"));
        guardar(new CategoriaGasto(null, "Alimentación", "VARIABLE"));
        guardar(new CategoriaGasto(null, "Transporte", "VARIABLE"));
        guardar(new CategoriaGasto(null, "Ropa", "VARIABLE"));
        guardar(new CategoriaGasto(null, "Educación", "FIJO"));
        guardar(new CategoriaGasto(null, "Suscripciones", "FIJO"));
        guardar(new CategoriaGasto(null, "Entretenimiento", "VARIABLE"));
        guardar(new CategoriaGasto(null, "Hogar", "FIJO"));
        guardar(new CategoriaGasto(null, "Salud", "VARIABLE"));
        guardar(new CategoriaGasto(null, "Mascotas", "VARIABLE"));
    }

    @Override
    public CategoriaGasto guardar(CategoriaGasto categoria) {
        // Asigna ID automáticamente si no tiene uno.
        if (categoria.getId() == null) {
            categoria.setId(secuenciaId++);

        }
        categorias.add(categoria);
        return categoria;
    }

    @Override
    public List<CategoriaGasto> obtenerTodas() {
        return  categorias;
    }

    @Override
    public Optional<CategoriaGasto> buscarPorId(Long id) {
        return categorias.stream().filter(c -> c.getId().equals(id)).findFirst();
    }
}
