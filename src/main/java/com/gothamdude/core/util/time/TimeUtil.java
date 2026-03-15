package com.gothamdude.core.util.time;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class TimeUtil {
    // Define Zones
    private static final ZoneId EST_ZONE = ZoneId.of("America/New_York");
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    // ISO-8601 Formatter
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Converts EST/EDT time string to UTC.
     */
    public static String convertEstToUtc(String estDateTime) {
        // Parse the input string as EST/EDT
        LocalDateTime localDateTime = LocalDateTime.parse(estDateTime, FORMATTER);
        ZonedDateTime estZonedDateTime = localDateTime.atZone(EST_ZONE);

        // Convert to UTC
        ZonedDateTime utcZonedDateTime = estZonedDateTime.withZoneSameInstant(UTC_ZONE);

        return utcZonedDateTime.format(FORMATTER);
    }

    /**
     * Converts UTC time string to EST/EDT.
     */
    public static String convertUtcToEst(String utcDateTime) {
        // Parse the input string as UTC
        LocalDateTime localDateTime = LocalDateTime.parse(utcDateTime, FORMATTER);
        ZonedDateTime utcZonedDateTime = localDateTime.atZone(UTC_ZONE);

        // Convert to EST/EDT
        ZonedDateTime estZonedDateTime = utcZonedDateTime.withZoneSameInstant(EST_ZONE);

        return estZonedDateTime.format(FORMATTER);
    }

    // Main method to test
    public static void main(String[] args) {
        String estTime = "2026-03-14 10:00:00"; // During EDT (UTC-4)
        String utcTime = convertEstToUtc(estTime);
        System.out.println("EST: " + estTime + " -> UTC: " + utcTime);

        String utcTimeBack = "2026-03-14 14:00:00"; // 2 PM UTC
        String estTimeBack = convertUtcToEst(utcTimeBack);
        System.out.println("UTC: " + utcTimeBack + " -> EST: " + estTimeBack);
    }

}
