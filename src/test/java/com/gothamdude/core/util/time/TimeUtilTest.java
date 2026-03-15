package com.gothamdude.core.util.time;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilTest {

    // -------------------------------------------------------------------------
    // 1. convertEstToUtc(String)
    // -------------------------------------------------------------------------

    // During EDT (Mar–Nov, UTC-4): +4 hours to reach UTC
    @Test
    void convertEstToUtc_duringEdt_addsForHours() {
        assertEquals("2024-06-15 14:00:00", TimeUtil.convertEstToUtc("2024-06-15 10:00:00"));
    }

    // During EST (Nov–Mar, UTC-5): +5 hours to reach UTC
    @Test
    void convertEstToUtc_duringEst_addsFiveHours() {
        assertEquals("2024-01-15 15:00:00", TimeUtil.convertEstToUtc("2024-01-15 10:00:00"));
    }

    // Conversion that crosses into the next calendar day
    @Test
    void convertEstToUtc_crossesMidnight_incrementsDay() {
        assertEquals("2024-06-16 02:00:00", TimeUtil.convertEstToUtc("2024-06-15 22:00:00"));
    }

    // Midnight in EDT → 04:00 UTC same day
    @Test
    void convertEstToUtc_midnight_returnsCorrectUtc() {
        assertEquals("2024-06-15 04:00:00", TimeUtil.convertEstToUtc("2024-06-15 00:00:00"));
    }

    // Confirms example from TimeUtil.main()
    @Test
    void convertEstToUtc_mainExampleTimestamp() {
        assertEquals("2026-03-14 14:00:00", TimeUtil.convertEstToUtc("2026-03-14 10:00:00"));
    }

    @Test
    void convertEstToUtc_invalidFormat_throwsDateTimeParseException() {
        assertThrows(DateTimeParseException.class,
                () -> TimeUtil.convertEstToUtc("06/15/2024 10:00:00"));
    }

    @Test
    void convertEstToUtc_nullInput_throwsException() {
        assertThrows(Exception.class, () -> TimeUtil.convertEstToUtc(null));
    }

    @Test
    void convertEstToUtc_emptyString_throwsException() {
        assertThrows(Exception.class, () -> TimeUtil.convertEstToUtc(""));
    }

    // -------------------------------------------------------------------------
    // 2. convertUtcToEst(String)
    // -------------------------------------------------------------------------

    // During EDT (Mar–Nov, UTC-4): -4 hours to reach EDT
    @Test
    void convertUtcToEst_duringEdt_subtractsFourHours() {
        assertEquals("2024-06-15 10:00:00", TimeUtil.convertUtcToEst("2024-06-15 14:00:00"));
    }

    // During EST (Nov–Mar, UTC-5): -5 hours to reach EST
    @Test
    void convertUtcToEst_duringEst_subtractsFiveHours() {
        assertEquals("2024-01-15 10:00:00", TimeUtil.convertUtcToEst("2024-01-15 15:00:00"));
    }

    // Conversion that crosses back into the previous calendar day
    @Test
    void convertUtcToEst_crossesMidnight_decrementsDay() {
        assertEquals("2024-06-15 22:00:00", TimeUtil.convertUtcToEst("2024-06-16 02:00:00"));
    }

    // Midnight UTC → 20:00 / 19:00 previous day in EST/EDT
    @Test
    void convertUtcToEst_midnightUtc_returnsPreviousDay() {
        // Midnight UTC on a summer date → 20:00 EDT the previous day
        assertEquals("2024-06-14 20:00:00", TimeUtil.convertUtcToEst("2024-06-15 00:00:00"));
    }

    // Confirms reverse of TimeUtil.main() example
    @Test
    void convertUtcToEst_mainExampleTimestamp() {
        assertEquals("2026-03-14 10:00:00", TimeUtil.convertUtcToEst("2026-03-14 14:00:00"));
    }

    @Test
    void convertUtcToEst_invalidFormat_throwsDateTimeParseException() {
        assertThrows(DateTimeParseException.class,
                () -> TimeUtil.convertUtcToEst("2024/06/15 14:00:00"));
    }

    @Test
    void convertUtcToEst_nullInput_throwsException() {
        assertThrows(Exception.class, () -> TimeUtil.convertUtcToEst(null));
    }

    @Test
    void convertUtcToEst_emptyString_throwsException() {
        assertThrows(Exception.class, () -> TimeUtil.convertUtcToEst(""));
    }

    // -------------------------------------------------------------------------
    // Round-trip consistency
    // -------------------------------------------------------------------------

    @Test
    void estToUtcToEst_roundTrip_duringEdt() {
        String original = "2024-06-15 10:00:00";
        String utc = TimeUtil.convertEstToUtc(original);
        assertEquals(original, TimeUtil.convertUtcToEst(utc));
    }

    @Test
    void estToUtcToEst_roundTrip_duringEst() {
        String original = "2024-01-15 10:00:00";
        String utc = TimeUtil.convertEstToUtc(original);
        assertEquals(original, TimeUtil.convertUtcToEst(utc));
    }

    @Test
    void utcToEstToUtc_roundTrip_duringEdt() {
        String original = "2024-06-15 14:00:00";
        String est = TimeUtil.convertUtcToEst(original);
        assertEquals(original, TimeUtil.convertEstToUtc(est));
    }

    @Test
    void utcToEstToUtc_roundTrip_duringEst() {
        String original = "2024-01-15 15:00:00";
        String est = TimeUtil.convertUtcToEst(original);
        assertEquals(original, TimeUtil.convertEstToUtc(est));
    }
}