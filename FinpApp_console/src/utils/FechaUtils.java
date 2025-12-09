package utils;

import java.time.LocalDate;

public class FechaUtils {

    // Primer día del mes.
    public static LocalDate inicioDeMes(int anio, int mes) {
        return LocalDate.of(anio, mes, 1);
    }

    // Último día del mes.
    public static LocalDate finDeMes(int anio, int mes) {
        LocalDate inicio = inicioDeMes(anio, mes);
        return inicio.withDayOfMonth(inicio.lengthOfMonth());
    }
}
