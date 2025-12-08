package ui;

import dominio.Movimiento;
import servicio.FinanzasService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MenuConsola {
    private final FinanzasService service;
    private final Scanner scanner = new Scanner(System.in);

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
                case 1 -> mostrarCategorias();
                case 2 -> registrarCategoria();
                case 3 -> registrarMovimiento();
                case 4 -> listarMovimientosMes();
                case 5 -> verResumen();
                case 6 -> configurarPresupuesto();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida");
            }

        } while (option != 0);
    }

    // Menú mostrado al usuario
    private void mostrarMenu() {
        System.out.println("""
                === FINPAPP - GESTOR FINANZAS ===
                1. Listar categorías
                2. Registrar nueva categoría
                3. Registrar movimiento
                4. Listar movimientos del mes
                5. Ver resumen del mes
                6. Configurar presupuesto mensual
                0. Salir
                ====================================
                """);
    }

    private void mostrarCategorias(){
        System.out.println("\n=== Categorías disponibles ===");
        service.obtenerCategorias().forEach(System.out::println);
        System.out.println();
    }

    private void registrarCategoria(){
        System.out.print("Nombre de la categoría: ");
        String nombre = scanner.nextLine();

        System.out.print("Tipo (FIJO/VARIABLE): ");
        String tipo = scanner.nextLine();

        service.registrarCategoria(nombre, tipo);
        System.out.println("Categoría creada correctamente.\n");
    }

    private void registrarMovimiento(){
        System.out.print("Tipo (INGRESO/GASTO): ");
        String tipo = scanner.nextLine();

        double monto = leerDouble("Monto: ");
        LocalDate fecha = LocalDate.parse(leerTexto("Fecha (YYYY-MM-DD): "));

        mostrarCategorias();

        Long categoriaId = leerLong("ID categoría: ");
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        service.registrarMovimiento(tipo, monto, fecha, categoriaId, descripcion);
        System.out.println("Movimiento registrado.\n");

    }

    private void listarMovimientosMes(){
        int anio = leerInt("Año: ");
        int mes = leerInt("Mes: ");

        List<Movimiento> movimientos = service.listarMovimientos(anio, mes);
        System.out.println("\n=== Movimientos del mes ===");
        movimientos.forEach(System.out::println);
        System.out.println();
    }

    private void verResumen(){
        int anio = leerInt("Año: ");
        int mes = leerInt("Mes: ");

        var resumen = service.obtenerResumen(anio, mes);
        System.out.println("\n=== Resumen Mensual ===");
        System.out.println("Ingresos: $" + resumen.totalIngresos);
        System.out.println("Gastos: $" + resumen.totalGastos);
        System.out.println("Saldo: $" + resumen.saldo);

        if (resumen.presupuestoMensual != null) {
            System.out.println("Presupuesto: $" + resumen.presupuestoMensual);

            if (resumen.totalGastos > resumen.presupuestoMensual) {
                System.out.println("⚠️ Advertencia: gasto supera el presupuesto asignado");
            }
        }

        System.out.println("\nGasto por categoría:");
        resumen.gastosPorCategoria.forEach((cat, val) ->
                System.out.println(cat + ": $" + val));
        System.out.println();
    }

    private void configurarPresupuesto() {
        int anio = leerInt("Año: ");
        int mes = leerInt("Mes: ");
        double monto = leerDouble("Monto presupuesto: ");

        service.configurarPresupuesto(anio, mes, monto);
        System.out.println("Presupuesto configurado.\n");

    }

    // Métodos auxiliares para leer datos del usuario
    private int leerInt(String msg) {
        System.out.println(msg);
        return Integer.parseInt(scanner.nextLine());
    }

    private double leerDouble(String msg) {
        System.out.println(msg);
        return Double.parseDouble(scanner.nextLine());
    }

    private long leerLong(String msg) {
        System.out.println(msg);
        return Long.parseLong(scanner.nextLine());
    }

    private String leerTexto(String msg) {
        System.out.println(msg);
        return scanner.nextLine();
    }
}
