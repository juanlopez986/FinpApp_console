package ui;

import dominio.CategoriaGasto;
import dominio.Movimiento;
import servicio.FinanzasService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MenuConsola {
    private final FinanzasService service;
    private final Scanner scanner = new Scanner(System.in);

    // Constantes para colores
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";

    public MenuConsola(FinanzasService service) {
        this.service = service;
    }

    // Bucle principal del programa
    public void iniciar() {
        int option;
        do {
            mostrarMenu();
            option = leerInt("Seleccione una opción: ");
            switch (option) {
                case 1 -> listarCategorias();
                case 2 -> registrarCategoria();
                case 3 -> registrarMovimiento();
                case 4 -> listarMovimientosMes();
                case 5 -> configurarPresupuesto();
                case 6 -> verResumen();
                case 7 -> compararGastosVsPresupuesto();
                case 0 -> printInfo("Saliendo del sistema...");
                default -> printWarn("Opción inválida");
            }

        } while (option != 0);
    }

    // Menú mostrado al usuario
    private void mostrarMenu() {
        System.out.println(BLUE + """
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
                """ + RESET);
    }

    // Categorías (CRUD)

    // READ -> Consultar
    private void listarCategorias(){
        printSection("Categorías disponibles");
        List<CategoriaGasto> categoriaGastos = service.listarCategorias();
        if (categoriaGastos.isEmpty()) {
            printInfo("No hay categorías registradas.\n");
        }else {
            categoriaGastos.forEach(System.out::println);
            System.out.println();
        }
    }

    // CREATE -> Guardar
    private void registrarCategoria(){
        printSection("Registrar categoría");
        String nombre = leerTexto("Nombre de la categoría: ");
        String tipo = leerTexto("Tipo (FIJO/VARIABLE): ").toUpperCase();
        String descripcion = leerTexto("Descripción: ");

        try {
            CategoriaGasto c = service.registrarCategoria(nombre, tipo, descripcion);
            printSuccess("Categoría creada con ID: " + c.getId() + "\n");
        } catch (IllegalArgumentException e) {
            printError("No se pudo crear la categoría: " + e.getMessage());
        }
    }

    // Movimientos (CRUD)

    // CREATE -> Guardar
    private void registrarMovimiento(){
        printSection("Registrar movimiento");

        String tipo = leerTexto("Tipo (INGRESO/GASTO): ").toUpperCase();
        double monto = leerDouble("Monto: ");
        String fechaStr = leerTexto("Fecha (YYYY-MM-DD): ");

        // Convertir solo si es válida
        LocalDate fecha = LocalDate.parse(fechaStr);

        listarCategorias(); // Carga las categorías disponibles
        Long categoriaId = leerLong("ID categoría: ");
        String descripcion = leerTexto("Descripción: ");

        try {
            Movimiento m = service.registrarMovimiento(tipo, monto, fecha, categoriaId, descripcion);
            printSuccess("Movimiento creado con ID: " + m.getId() );
        } catch (RuntimeException e) {
            printError("No se pudo crear movimiento: " + e.getMessage());
        }
    }

    // READ -> Consultar
    private void listarMovimientosMes(){
        printSection("Listar movimientos por mes");
        int anio = leerInt("Año: ");
        int mes = leerInt("Mes: ");
        List<Movimiento> movimientos = service.obtenerMovimientosMes(anio, mes);
        if (movimientos.isEmpty()) {
            printInfo("No hay movimientos para ese mes.\n");
        } else {
            movimientos.forEach(System.out::println);
            System.out.println();
        }
    }

    // Presupuesto y resumen mensual
    private void configurarPresupuesto() {
        printSection("Configurar presupuesto");
        int anio = leerInt("Año: ");
        int mes = leerInt("Mes: ");
        double monto = leerDouble("Monto presupuesto: ");

        try {
            service.configurarPresupuesto(anio, mes, monto);
            printSuccess("Presupuesto configurado para " + mes  + "-" + anio + ".");
        } catch (RuntimeException e) {
            printError("No se pudo configurar presupuesto: " + e.getMessage());
        }
    }

    private void verResumen(){
        printSection("Resumen Mensual");
        int anio = leerInt("Año: ");
        int mes = leerInt("Mes: ");
        var resumen = service.obtenerResumen(anio, mes);

        System.out.println("\nIngresos: $" + resumen.totalIngresos);
        System.out.println("Gastos: $" + resumen.totalGastos);
        System.out.println("Saldo: $" + resumen.saldo);

        if (resumen.presupuestoMensual != null) {
            System.out.println("Presupuesto: $" + resumen.presupuestoMensual);
            if (resumen.totalGastos > resumen.presupuestoMensual) {
                printWarn("Gastos superan el presupuesto asignado");
            }
        }

        System.out.println("\nGastos por categoría:");
        if (resumen.gastosPorCategoria == null || resumen.gastosPorCategoria.isEmpty()) {
            printWarn("No hay gastos registrados.\n");
        } else {
            resumen.gastosPorCategoria.forEach((cat, val) ->
                    System.out.println(cat + ": $" + val));
            System.out.println();
        }
    }

    // Comparar gastos vs presupuesto
    private void compararGastosVsPresupuesto() {
        printSection("Comparar gastos vs presupuesto");
        int anio = leerInt("Año: ");
        int mes = leerInt("Mes: ");

        var resumen = service.obtenerResumen(anio, mes);
        if (resumen.presupuestoMensual == null) {
            printWarn("No hay presupuesto configurado para este mes.\n");
            return;
        }
        double presupuesto = resumen.presupuestoMensual;
        double gastos = resumen.totalGastos;
        double porcentaje = (presupuesto == 0) ? 0 : (gastos / presupuesto) * 100;

        System.out.println("\n📅 Mes " + mes + " de " + anio);
        System.out.println("Presupuesto: $" + presupuesto);
        System.out.println("Gastos: $" + gastos);

        // Representación gráfica simple tipo barra de progreso (20 caracteres)
        String barra = barraProgreso(presupuesto, gastos, 20);
        String color;
        if (porcentaje < 80){
            color = GREEN;
        } else if (porcentaje < 100){
            color = YELLOW;
        } else {
            color = RED;
        }

        System.out.println(color + "[" + barra + "]" + RESET);

        // Evaluación del estado financiero
        if (gastos < presupuesto * 0.80) {
            System.out.println("Estado: Dentro del presupuesto" + color + " ("
                    + String.format("%.1f", + porcentaje) + "% usado)" + RESET);
        } else if (gastos < presupuesto) {
            System.out.println("Estás cerca del límite" + color + " ("
                    + String.format("%.1f", + porcentaje) + "% usado)" + RESET);
        } else {
            System.out.println("Alerta: Te pasaste del presupuesto" + color + " ("
                    + String.format("%.1f", + porcentaje) + "% usado)" + RESET);
        }

        // Mostrar diferencia
        double diferencia = (presupuesto - gastos);
        if (diferencia >= 0) {
            System.out.println("Te quedan: $" + diferencia + " del presupuesto.\n");
        } else {
            System.out.println("Excediste por: $" + (-diferencia) + "\n");
        }
    }

    // Barra de progreso textual: calcula cuantos bloques llenar
    private String barraProgreso(double objetivo, double actual, int longitud) {
        if (objetivo <= 0) {
            // Si objetivo 0, la barra muestra completamente llena si hay gasto, o vacía si no hay gasto.
            return (actual > 0) ? "#".repeat(longitud) : "-".repeat(longitud);
        }
        double ratio = actual / objetivo;
        int llenos = (int) Math.round(Math.min(ratio, 1.0) * longitud);
        String llenosStr = "#".repeat(llenos);
        String vaciosStr = "-".repeat(Math.max(0, longitud - llenos));
        return llenosStr + vaciosStr;
    }

    // Métodos auxiliares para leer datos del usuario
    private int leerInt(String msg) {
        System.out.print(msg);
        return Integer.parseInt(scanner.nextLine());
    }

    private double leerDouble(String msg) {
        System.out.print(msg);
        return Double.parseDouble(scanner.nextLine());
    }

    private long leerLong(String msg) {
        System.out.print(msg);
        return Long.parseLong(scanner.nextLine());
    }

    private String leerTexto(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }

    // Métodos de impresión con colores en consola
    private void printSuccess(String msg) {
        System.out.println(GREEN + "✅ " + msg + RESET);
    }

    private void printError(String msg) {
        System.out.println(RED + "❌ " + msg + RESET);
    }

    private void printWarn(String msg) {
        System.out.println(YELLOW + "⚠️ " + msg + RESET);
    }

    private void printInfo(String msg) {
        System.out.println(CYAN + msg + RESET);
    }

    private void printSection(String title) {
        System.out.println(PURPLE + "\n=== " + title + " ===" + RESET);
    }
}
