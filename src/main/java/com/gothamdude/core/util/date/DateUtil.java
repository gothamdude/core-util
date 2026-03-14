package com.gothamdude.core.util.date;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    // Define standard formats
    private static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);

    // 1. Get current date as String
    public static String getCurrentDateStr() {
        return LocalDate.now().format(dtf);
    }

    // 2. Format a date
    public static String formatDate(LocalDate date) {
        return date.format(dtf);
    }

    // 3. Parse String to LocalDate
    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, dtf);
    }

    // 4. Add days to date
    public static LocalDate addDays(LocalDate date, int days) {
        return date.plusDays(days);
    }

    // 5. Convert Old Date to New LocalDate
    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // 6. Convert New LocalDate to Old Date
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
