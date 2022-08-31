package io.github.melin.spark.jobserver.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.systemDefault());

    public static String formateDateTime(Instant dateTime) {
        if (dateTime == null) {
            return "";
        }
        return DATE_TIME_FORMATTER.format(dateTime);
    }

    public static String formateDateTime(LocalDateTime dateTime, String formate) {
        if (dateTime == null) {
            return "";
        }
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern(formate);
            return dateTime.format(format);
        } catch (DateTimeException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static String formateDate(Instant dateTime) {
        if (dateTime == null) {
            return "";
        }
        return DATE_FORMATTER.format(dateTime);
    }

    public static String getCurrentDate() {
        try {
            LocalDateTime ldt = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
            return ldt.format(format);
        } catch (DateTimeException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static String getCurrentDateTime() {
        try {
            LocalDateTime ldt = LocalDateTime.now();
            return ldt.format(DATE_TIME_FORMATTER);
        } catch (DateTimeException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
