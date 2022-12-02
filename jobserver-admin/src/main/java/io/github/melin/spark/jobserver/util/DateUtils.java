package io.github.melin.spark.jobserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DateUtils.class);

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.systemDefault());

    public static String formatDateTime(Instant dateTime) {
        if (dateTime == null) {
            return "";
        }
        return DATE_TIME_FORMATTER.format(dateTime);
    }

    public static String formatDateTime(LocalDateTime dateTime, String formate) {
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

    public static String formatDate(Instant dateTime) {
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

    public static String formatTimestamp(long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return sdf.format(new Date(time));
        } catch (Exception e) {
            LOG.error("时间转换出错: " + time, e);
        }
        return "";
    }
}
