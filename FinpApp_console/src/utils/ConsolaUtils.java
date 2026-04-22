package utils;

import java.util.Scanner;

public class ConsolaUtils {
    // Constantes para colores ANSI
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsolaUtils() {
        // Evita instalación
    }

    // Colores por porcentaje
    public static String colorPorcentaje(double porcentaje, double limiteVerde, double limiteAmarillo) {
        if (porcentaje < limiteVerde){
            return GREEN;
        } else if (porcentaje <= limiteAmarillo){
            return YELLOW;
        } else {
            return RED;
        }
    }

    // Barra de progreso
    public static String barraProgreso(double objetivo, double actual, int longitud) {
        if (objetivo <= 0) {
            return "-".repeat(longitud);
        }

        double ratio = Math.min(actual / objetivo, 1.0); // límite 100%
        int llenos = (int) Math.round(ratio * longitud);
        String llenosStr = "#".repeat(llenos);
        String vaciosStr = "-".repeat(longitud - llenos);

        return llenosStr + vaciosStr;
    }

    // Barra de estado más porcentaje
    public static double MostrarBarraEstado(double actual, double objetivo, int longitud,
                                            double limiteVerde, double limiteAmarillo) {
        // Evitar división por cero
        double porcentaje = (objetivo <= 0) ? 0 : (actual / objetivo) * 100;
        String color = colorPorcentaje(porcentaje, limiteVerde, limiteAmarillo);
        String barra = barraProgreso(objetivo, actual, longitud);

        System.out.printf("%s [%s] %.1f%%%s%n", color, barra, Math.min(porcentaje, 100), RESET);
        return porcentaje;
    }

    // Métodos de impresión con colores en consola
    public static void printSuccess(String msg) {
        System.out.println(GREEN + "✅ " + msg + RESET);
    }

    public static void printError(String msg) {
        System.out.println(RED + "❌ " + msg + RESET);
    }

    public static void printWarn(String msg) {
        System.out.println(YELLOW + "⚠️ " + msg + RESET);
    }

    public static void printInfo(String msg) {
        System.out.println(CYAN + msg + RESET);
    }

    public static void printSection(String title) {
        System.out.println(PURPLE + "=== " + title + " ===" + RESET);
    }

    // Métodos auxiliares para leer datos del usuario
    public static int leerInt(String msg) {
        System.out.print(msg);
        return Integer.parseInt(SCANNER.nextLine());
    }

    public static double leerDouble(String msg) {
        System.out.print(msg);
        return Double.parseDouble(SCANNER.nextLine());
    }

    public static long leerLong(String msg) {
        System.out.print(msg);
        return Long.parseLong(SCANNER.nextLine());
    }

    public static String leerTexto(String msg) {
        System.out.print(msg);
        return SCANNER.nextLine();
    }
}
