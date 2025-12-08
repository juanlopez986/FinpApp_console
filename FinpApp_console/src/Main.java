import repositotio.memoria.CategoriaRepositoryInMemory;
import repositotio.memoria.MovimientoRepositoryInMemory;
import repositotio.memoria.PresupuestoRepositoryInMemory;
import servicio.FinanzasService;
import ui.MenuConsola;

public class Main {
    public static void main(String[] args) {

        // Repositorios en memoria
        var categoriaRepository = new CategoriaRepositoryInMemory();
        var movimientoRepository = new MovimientoRepositoryInMemory();
        var presupuestoRepository = new PresupuestoRepositoryInMemory();

        // Capa de servicio (reglas de negocio)
        var service = new FinanzasService(categoriaRepository, movimientoRepository, presupuestoRepository);

        // Menú de usuario
        var menu = new MenuConsola(service);

        // Iniciar aplicación
        menu.iniciar();
    }
}