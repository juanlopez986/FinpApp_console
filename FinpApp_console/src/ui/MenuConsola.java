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
                case 5 -> verResumen();
                case 6 -> configurarPresupuesto();
                case 7 -> compararGastosVsPresupuesto();
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
                3. Registrar movimiento (INGRESO/GASTO)
                4. Listar movimientos del mes
                5. Ver resumen del mes
                6. Configurar presupuesto mensual
                7. Comparar gastos vs presupuesto
                0. Salir
                ====================================
                """);
    }

    private void listarCategorias(){
        System.out.println("\n=== Categorías disponibles ===");
        service.obtenerCategorias().forEach(System.out::println);
        System.out.println();
    }

    private void registrarCategoria(){
        System.out.println("\n=== Registrar categoría ===");
        String nombre = leerTexto("Nombre de la categoría: ");
        String tipo = leerTexto("Tipo (FIJO/VARIABLE): ");

        CategoriaGasto c = service.registrarCategoria(nombre, tipo);
        System.out.println("Creada categoría con ID: " + c.getId() + "\n");
    }

    private void registrarMovimiento(){
        System.out.println("\n=== Registrar movimiento ===");
        String tipo = leerTexto("Tipo (INGRESO/GASTO): ");
        double monto = leerDouble("Monto: ");
        String fechaStr = leerTexto("Fecha (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(fechaStr);

        listarCategorias();
        Long categoriaId = leerLong("ID categoría: ");
        String descripcion = leerTexto("Descripción: ");

        service.registrarMovimiento(tipo, monto, fecha, categoriaId, descripcion);
        System.out.println("Movimiento registrado.\n");
    }

    private void listarMovimientosMes(){
        System.out.println("\n=== Listar movimientos por mes ===");
        int anio = leerInt("Año: ");
        int mes = leerInt("Mes: ");
        List<Movimiento> movimientos = service.listarMovimientos(anio, mes);
        if (movimientos.isEmpty()) {
            System.out.println("No hay movimientos para ese mes.\n");
        } else {
            movimientos.forEach(System.out::println);
            System.out.println();
        }
    }

    private void verResumen(){
        System.out.println("\n=== Resumen Mensual ===");
        int anio = leerInt("Año: ");
        int mes = leerInt("Mes: ");
        var resumen = service.obtenerResumen(anio, mes);

        System.out.println("Ingresos: $" + resumen.totalIngresos);
        System.out.println("Gastos: $" + resumen.totalGastos);
        System.out.println("Saldo: $" + resumen.saldo);

        if (resumen.presupuestoMensual != null) {
            System.out.println("Presupuesto: $" + resumen.presupuestoMensual);
            if (resumen.totalGastos > resumen.presupuestoMensual) {
                System.out.println("⚠️ Gastos superan el presupuesto asignado");
            }
        }

        System.out.println("\nGasto por categoría:");
        resumen.gastosPorCategoria.forEach((cat, val) ->
                System.out.println(cat + ": $" + val));
        System.out.println();
    }

    private void configurarPresupuesto() {
        System.out.println("\n=== Configurar presupuesto ===");
        int anio = leerInt("Año: ");
        int mes = leerInt("Mes: ");
        double monto = leerDouble("Monto presupuesto: ");

        service.configurarPresupuesto(anio, mes, monto);
        System.out.println("Presupuesto configurado.\n");
    }

    private void compararGastosVsPresupuesto() {
        System.out.println("\n=== Comparar gastos vs presupuesto ===");
        int anio = leerInt("Año: ");
        int mes = leerInt("Mes: ");

        var resumen = service.obtenerResumen(anio, mes);
        if (resumen.presupuestoMensual == null) {
            System.out.println("No hay presupuesto configurado para este mes.\n");
            return;
        }
        double presupuesto = resumen.presupuestoMensual;
        double gastos = resumen.totalGastos;
        System.out.println("\n Mes " + mes + " de " + anio);
        System.out.println("Presupuesto: " + presupuesto);
        System.out.println("Gastos: " + gastos);

        double porcentaje = (gastos / presupuesto) * 100;

        // Evaluación del estado financiero
        if (gastos < presupuesto * 0.80) {
            System.out.println("Estado: Dentro del presupuesto (" + String.format("%.1f", + porcentaje) + "% usado)");
        } else if (gastos < presupuesto) {
            System.out.println("Estás cerca del límite (" + String.format("%.1f", + porcentaje) + "% usado)");
        } else {
            System.out.println("Alerta: Te pasaste del presupuesto (" + String.format("%.1f", + porcentaje) + "% usado)");
        }

        // Mostrar diferencia
        double diferencia = presupuesto - gastos;
        if (diferencia >= 0) {
            System.out.println("Te quedan: $" + diferencia + " del presupuesto.\n");
        } else {
            System.out.println("Excediste por: $" + (-diferencia) + "\n");
        }
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
}
