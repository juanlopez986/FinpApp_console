package utils;

public class ConsolaUtils {

    // Constantes para colores ANSI
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";

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

        System.out.println(color + "[" + barra + "]" +
                String.format("%.1f", Math.min(porcentaje, 100)) + "%" + RESET);
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
}
