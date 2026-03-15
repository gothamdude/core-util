package com.gothamdude.core.util.date;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    // -------------------------------------------------------------------------
    // 1. getCurrentDateStr()
    // -------------------------------------------------------------------------

    @Test
    void getCurrentDateStr_returnsStringInExpectedFormat() {
        String result = DateUtil.getCurrentDateStr();
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}"),
                "Expected format yyyy-MM-dd but got: " + result);
    }

    @Test
    void getCurrentDateStr_matchesTodayDate() {
        String expected = LocalDate.now().toString(); // already yyyy-MM-dd
        assertEquals(expected, DateUtil.getCurrentDateStr());
    }

    // -------------------------------------------------------------------------
    // 2. formatDate(LocalDate)
    // -------------------------------------------------------------------------

    @Test
    void formatDate_returnsCorrectlyFormattedString() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        assertEquals("2024-06-15", DateUtil.formatDate(date));
    }

    @Test
    void formatDate_paddingForSingleDigitMonthAndDay() {
        LocalDate date = LocalDate.of(2000, 1, 5);
        assertEquals("2000-01-05", DateUtil.formatDate(date));
    }

    @Test
    void formatDate_leapDay() {
        LocalDate date = LocalDate.of(2024, 2, 29);
        assertEquals("2024-02-29", DateUtil.formatDate(date));
    }

    // -------------------------------------------------------------------------
    // 3. parseDate(String)
    // -------------------------------------------------------------------------

    @Test
    void parseDate_validString_returnsCorrectLocalDate() {
        LocalDate result = DateUtil.parseDate("2024-06-15");
        assertEquals(LocalDate.of(2024, 6, 15), result);
    }

    @Test
    void parseDate_invalidFormat_throwsDateTimeParseException() {
        assertThrows(DateTimeParseException.class, () -> DateUtil.parseDate("15-06-2024"));
    }

    @Test
    void parseDate_nullInput_throwsException() {
        assertThrows(Exception.class, () -> DateUtil.parseDate(null));
    }

    @Test
    void parseDate_emptyString_throwsException() {
        assertThrows(Exception.class, () -> DateUtil.parseDate(""));
    }

    // -------------------------------------------------------------------------
    // 4. addDays(LocalDate, int)
    // -------------------------------------------------------------------------

    @Test
    void addDays_positiveValue_returnsDateInFuture() {
        LocalDate base = LocalDate.of(2024, 1, 1);
        assertEquals(LocalDate.of(2024, 1, 11), DateUtil.addDays(base, 10));
    }

    @Test
    void addDays_negativeValue_returnsDateInPast() {
        LocalDate base = LocalDate.of(2024, 3, 15);
        assertEquals(LocalDate.of(2024, 3, 5), DateUtil.addDays(base, -10));
    }

    @Test
    void addDays_zero_returnsSameDate() {
        LocalDate base = LocalDate.of(2024, 6, 15);
        assertEquals(base, DateUtil.addDays(base, 0));
    }

    @Test
    void addDays_crossesMonthBoundary() {
        LocalDate base = LocalDate.of(2024, 1, 30);
        assertEquals(LocalDate.of(2024, 2, 1), DateUtil.addDays(base, 2));
    }

    @Test
    void addDays_crossesYearBoundary() {
        LocalDate base = LocalDate.of(2023, 12, 30);
        assertEquals(LocalDate.of(2024, 1, 2), DateUtil.addDays(base, 3));
    }

    // -------------------------------------------------------------------------
    // 5. toLocalDate(Date)
    // -------------------------------------------------------------------------

    @Test
    void toLocalDate_convertsLegacyDateToLocalDate() {
        LocalDate expected = LocalDate.of(2024, 6, 15);
        Date legacyDate = Date.from(expected.atStartOfDay(ZoneId.systemDefault()).toInstant());
        assertEquals(expected, DateUtil.toLocalDate(legacyDate));
    }

    @Test
    void toLocalDate_nullInput_throwsException() {
        assertThrows(Exception.class, () -> DateUtil.toLocalDate(null));
    }

    // -------------------------------------------------------------------------
    // 6. toDate(LocalDate)
    // -------------------------------------------------------------------------

    @Test
    void toDate_convertsLocalDateToLegacyDate() {
        LocalDate localDate = LocalDate.of(2024, 6, 15);
        Date result = DateUtil.toDate(localDate);
        assertNotNull(result);
        // Round-trip: converting back should yield the same LocalDate
        assertEquals(localDate, DateUtil.toLocalDate(result));
    }

    @Test
    void toDate_nullInput_throwsException() {
        assertThrows(Exception.class, () -> DateUtil.toDate(null));
    }

    // -------------------------------------------------------------------------
    // Round-trip consistency
    // -------------------------------------------------------------------------

    @Test
    void formatAndParse_roundTrip() {
        LocalDate original = LocalDate.of(2024, 6, 15);
        String formatted = DateUtil.formatDate(original);
        LocalDate parsed = DateUtil.parseDate(formatted);
        assertEquals(original, parsed);
    }

    @Test
    void toDateAndToLocalDate_roundTrip() {
        LocalDate original = LocalDate.of(2024, 6, 15);
        Date converted = DateUtil.toDate(original);
        LocalDate result = DateUtil.toLocalDate(converted);
        assertEquals(original, result);
    }
}