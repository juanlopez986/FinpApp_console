package repositotio.memoria;

import dominio.CategoriaGasto;
import repositotio.CategoriaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CategoriaRepositoryInMemory implements CategoriaRepository {
    private final List<CategoriaGasto> categorias = new ArrayList<>();
    private long secuenciaId = 1L; // Genera IDs automáticamente

    public CategoriaRepositoryInMemory() {
        // CATEGORÍAS INICIALES (se cargan automáticamente)
        guardar(new CategoriaGasto(null,"Tecnología","VARIABLE",
                "Dispositivos, apps y mantenimiento digital"));
        guardar(new CategoriaGasto(null,"Alimentación","VARIABLE",
                "Comida, bebidas y mercado"));
        guardar(new CategoriaGasto(null,"Transporte","VARIABLE",
                "Movilidad urbana: bus, taxi, gasolina"));
        guardar(new CategoriaGasto(null,"Ropa","VARIABLE",
                " Prendas y accesorios"));
        guardar(new CategoriaGasto(null,"Educación","FIJO",
                "Cursos, matrícula y libros"));
        guardar(new CategoriaGasto(null,"Suscripciones",
                "FIJO","Servicios mensuales (Netflix, Spotify, etc."));
        guardar(new CategoriaGasto(null,"Entretenimiento",
                "VARIABLE","Cine, juegos, salidas"));
        guardar(new CategoriaGasto(null,"Hogar","FIJO",
                "Arriendo, servicios, mantenimiento"));
        guardar(new CategoriaGasto(null,"Salud","VARIABLE",
                "Medicinas, citas médicas"));
        guardar(new CategoriaGasto(null,"Mascotas","VARIABLE",
                "Comida y cuidado de mascotas"));
    }

    @Override
    public CategoriaGasto guardar(CategoriaGasto categoria) {
        // Asignar el ID si no tiene uno.
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
        // Convertir lista categorías a stream para procesarla
        return categorias.stream()
                // Filtrar por id categorías si es igual al id que buscamos
                // Objects.ecuals evita errores si alguno de los dos valores es null
                .filter(c -> Objects.equals(c.getId(), id))
                // Devuelve el primer valor si cumple con el fltro si no lo devuelve vacío
                .findFirst();
    }
}
