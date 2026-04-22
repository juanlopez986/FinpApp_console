package ui;

import dominio.CategoriaGasto;
import dominio.Movimiento;
import dominio.PresupuestoMensual;
import dominio.ResumenMensual;
import servicio.FinanzasService;
import utils.ConsolaUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class MenuConsola {
    private final FinanzasService service;

    public MenuConsola(FinanzasService service) {
        this.service = service;
    }

    // Bucle principal del programa
    public void iniciar() {
        int option;
        do {
            mostrarMenu();
            option = ConsolaUtils.leerInt("Seleccione una opción: ");
            switch (option) {
                case 1 -> listarCategorias();
                case 2 -> registrarCategoria();
                case 3 -> registrarMovimiento();
                case 4 -> listarMovimientosMes();
                case 5 -> configurarPresupuesto();
                case 6 -> verResumen();
                case 7 -> compararGastosVsPresupuesto();
                case 0 -> ConsolaUtils.printInfo("Saliendo del sistema...");
                default -> ConsolaUtils.printWarn("Opción inválida");
            }
        } while (option != 0);
    }

    // Menú mostrado al usuario
    private void mostrarMenu() {
        System.out.println(ConsolaUtils.BLUE + """
                === FINPAPP - GESTOR FINANZAS ===
                1. Listar categorías
                2. Registrar categoría
                3. Registrar movimiento (INGRESO/GASTO)
                4. Listar movimientos del mes
                5. Configurar presupuesto mensual
                6. Ver resumen Mensual
                7. Comparar gastos vs presupuesto
                0. Salir
                =================================
                """ + ConsolaUtils.RESET);
    }

    // Categorías (CRUD)

    // READ -> Consultar
    private void listarCategorias(){
        ConsolaUtils.printSection("Categorías disponibles");
        System.out.println();

        List<CategoriaGasto> categoriaGastos = service.listarCategorias();

        if (categoriaGastos.isEmpty()) {
            ConsolaUtils.printInfo("No hay categorías registradas.");
            System.out.println();
            return;
        }
        categoriaGastos.forEach(System.out::println);
        System.out.println();
    }

    // CREATE -> Guardar
    private void registrarCategoria(){
        ConsolaUtils.printSection("Registrar categoría");
        System.out.println();

        String nombre = ConsolaUtils.leerTexto("Nombre de la categoría: ");
        String tipo = ConsolaUtils.leerTexto("Tipo (FIJO/VARIABLE): ");
        String descripcion = ConsolaUtils.leerTexto("Descripción: ");

        try {
            CategoriaGasto c = service.registrarCategoria(nombre, tipo, descripcion);
            ConsolaUtils.printSuccess("Categoría creada con ID: " + c.getId());
        } catch (IllegalArgumentException e) {
            ConsolaUtils.printError("No se pudo crear la categoría: " + e.getMessage());
        }
    }

    // Movimientos (CRUD)

    // CREATE -> Guardar
    private void registrarMovimiento(){
        ConsolaUtils.printSection("Registrar movimiento");
        System.out.println();

        String tipo = ConsolaUtils.leerTexto("Tipo (INGRESO/GASTO): ");
        double monto = ConsolaUtils.leerDouble("Monto: ");
        String fechaStr = ConsolaUtils.leerTexto("Fecha (YYYY-MM-DD): ");

        // Convertir solo si es válida
        LocalDate fecha = LocalDate.parse(fechaStr);

        listarCategorias(); // Carga las categorías disponibles
        Long categoriaId = ConsolaUtils.leerLong("ID categoría: ");
        String descripcion = ConsolaUtils.leerTexto("Descripción: ");

        try {
            Movimiento m = service.registrarMovimiento(tipo, monto, fecha, categoriaId, descripcion);
            ConsolaUtils.printSuccess("Movimiento creado con ID: " + m.getId());
        } catch (RuntimeException e) {
            ConsolaUtils.printError("No se pudo crear movimiento: " + e.getMessage());
        }
    }

    // READ -> Consultar
    private void listarMovimientosMes(){
        ConsolaUtils.printSection("Listar movimientos por mes");
        System.out.println();

        int anio = ConsolaUtils.leerInt("Año: ");
        int mes = ConsolaUtils.leerInt("Mes: ");
        List<Movimiento> movimientos = service.listarMovimientosMes(anio, mes);

        if (movimientos.isEmpty()) {
            ConsolaUtils.printInfo("No hay movimientos para ese mes.");
            System.out.println();
            return;
        }
        movimientos.forEach(System.out::println);
        System.out.println();
    }

    // Presupuesto y resumen mensual
    private void configurarPresupuesto() {
        ConsolaUtils.printSection("Configurar presupuesto");
        System.out.println();

        int anio = ConsolaUtils.leerInt("Año: ");
        int mes = ConsolaUtils.leerInt("Mes: ");
        double monto = ConsolaUtils.leerDouble("Monto presupuesto: ");

        try {
            PresupuestoMensual p = service.configurarPresupuesto(anio, mes, monto);
            ConsolaUtils.printSuccess("Presupuesto configurado para " + p.getMes() + "-" + p.getAnio() + ".");
        } catch (RuntimeException e) {
            ConsolaUtils.printError("No se pudo configurar presupuesto: " + e.getMessage());
        }
    }

    private void verResumen(){
        ConsolaUtils.printSection("Resumen Mensual");
        System.out.println();

        int anio = ConsolaUtils.leerInt("Año: ");
        int mes = ConsolaUtils.leerInt("Mes: ");
        ResumenMensual resumen = service.obtenerResumen(anio, mes);

        double totalIngresos = resumen.getTotalIngresos();
        double totalGastos  = resumen.getTotalGastos();
        double saldo = resumen.getSaldo();

        System.out.printf("Ingresos: $%.2f%n", totalIngresos);
        System.out.printf("Gastos: $%.2f%n", totalGastos);
        if (saldo < 0) {
            System.out.printf("Déficit: $%.2f%n", Math.abs(saldo));
        } else {
            System.out.printf("Saldo: $%.2f%n", saldo);
        }

        Double presupuestoMensual = resumen.getPresupuestoMensual();
        if (presupuestoMensual != null) {
            System.out.printf("Presupuesto: $%.2f%n", presupuestoMensual);
            if (totalGastos > presupuestoMensual) {
                ConsolaUtils.printWarn("Gastos superan el presupuesto asignado");
            }
        }
        System.out.println();

        System.out.println("📊 Gastos por categoría:");
        Map<String,Double> gastosPorCategoria  = resumen.getGastosPorCategoria();

        if (gastosPorCategoria == null || gastosPorCategoria.isEmpty()) {
            ConsolaUtils.printWarn("No hay gastos registrados.");
            System.out.println();
            return;
        }

        String categoriaDominante = "";
        double mayorPorcentaje = 0;

        for (Map.Entry<String, Double> entry : gastosPorCategoria.entrySet()) {
            String categoria = entry.getKey(); // getKey -> Categoría
            double monto = entry.getValue(); // getValue -> Monto

            double porcentaje = (totalGastos > 0) ? (monto / totalGastos) * 100 : 0;

            if (porcentaje > mayorPorcentaje) {
                mayorPorcentaje = porcentaje;
                categoriaDominante = categoria;
            }

            String color = ConsolaUtils.colorPorcentaje(porcentaje, 40, 60);
            String barra = ConsolaUtils.barraProgreso(totalGastos, monto, 20);

            System.out.printf("%s%s | $%.2f | [%s] %.1f%%%s%n",
                    color, categoria, monto, barra, porcentaje, ConsolaUtils.RESET);
        }

        System.out.println("-------------------------");
        System.out.printf("Total gastos: $%.2f%n", totalGastos);

        if (mayorPorcentaje > 50) {
            System.out.printf("%sLa categoría '%s' consume el %.1f%% de tus gastos.%s%n",
                    ConsolaUtils.YELLOW, categoriaDominante, mayorPorcentaje, ConsolaUtils.RESET);
        }
        System.out.println();
    }

    // Comparar gastos vs presupuesto
    private void compararGastosVsPresupuesto() {
        ConsolaUtils.printSection("Comparar gastos vs presupuesto");
        System.out.println();

        int anio = ConsolaUtils.leerInt("Año: ");
        int mes = ConsolaUtils.leerInt("Mes: ");
        ResumenMensual resumen = service.obtenerResumen(anio, mes);

        Double presupuestoMensual = resumen.getPresupuestoMensual();
        double totalGastos = resumen.getTotalGastos();

        if (presupuestoMensual == null) {
            ConsolaUtils.printWarn("No hay presupuesto configurado para este mes.");
            System.out.println();
            return;
        }

        System.out.println("📅 Mes " + mes + " de " + anio);
        System.out.printf("Presupuesto: $%.2f%n", presupuestoMensual);
        System.out.printf("Gastos: $%.2f%n", totalGastos);
        System.out.println();

        double porcentaje = ConsolaUtils.MostrarBarraEstado(totalGastos, presupuestoMensual,
                20, 80, 100);

        // Evaluación del estado financiero
        if ( totalGastos > presupuestoMensual) {
            System.out.println(ConsolaUtils.RED + "Alerta: Te pasaste del presupuesto" + ConsolaUtils.RESET);
        } else if (porcentaje >= 80) {
            System.out.println(ConsolaUtils.YELLOW + "Advertencia: Cerca del límite" + ConsolaUtils.RESET);
        } else {
            System.out.println(ConsolaUtils.GREEN + "Estado: Dentro del presupuesto" + ConsolaUtils.RESET);
        }

        // Mostrar diferencia
        double diferencia = presupuestoMensual - totalGastos;
        System.out.println("Diferencia financiera:");
        if (diferencia >= 0) {
            System.out.printf("%sTe quedan: $%.2f%s%n", ConsolaUtils.GREEN, diferencia, ConsolaUtils.RESET);
        } else {
            System.out.printf("%sExcediste por: $%.2f%s%n", ConsolaUtils.RED, Math.abs(diferencia), ConsolaUtils.RESET);
        }
    }
}
