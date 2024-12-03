package com.encore.frab.reto.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LocalDateToISO8601 {

    public static String formatUsingDateTimeFormatter(LocalDateTime localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        return localDate.atOffset(ZoneOffset.UTC).format(formatter);
    }

}
