import repositorio.memoria.CategoriaRepositoryInMemory;
import repositorio.memoria.MovimientoRepositoryInMemory;
import repositorio.memoria.PresupuestoRepositoryInMemory;
import servicio.FinanzasService;
import ui.MenuConsola;

public class Main {
    public static void main(String[] args) {
        // 1. Crear repositorios en memoria (simulación de base de datos)
        var categoriaRepository = new CategoriaRepositoryInMemory();
        var movimientoRepository = new MovimientoRepositoryInMemory();
        var presupuestoRepository = new PresupuestoRepositoryInMemory();

        // 2. Crear el servicio y pasarle los repositorios (inyección de dependencias)
        var service = new FinanzasService(
                categoriaRepository,
                movimientoRepository,
                presupuestoRepository
        );

        // 3. Crear el menú pasándole el servicio
        var menu = new MenuConsola(service);

        // 4. Iniciar la aplicación
        menu.iniciar();
    }
}