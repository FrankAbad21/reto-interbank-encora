package com.encore.frab.reto.util;

public class GeneradorId {

    public static String generarIdDiezDigitosUnicos() {
        String idUnico = String.valueOf(System.currentTimeMillis());
        return idUnico.substring(idUnico.length() -10, idUnico.length());
    }

}
