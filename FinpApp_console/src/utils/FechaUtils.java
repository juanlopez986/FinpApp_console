package utils;

import java.time.LocalDate;

public class FechaUtils {
    public  static LocalDate inicioDeMes(int anio, int mes) {
        return LocalDate.of(anio, mes, 1);
    }

    public  static LocalDate finDeMes(int anio, int mes) {
        LocalDate inicio = inicioDeMes(anio, mes);
        return inicio.withDayOfMonth(inicio.lengthOfMonth());
    }
}
