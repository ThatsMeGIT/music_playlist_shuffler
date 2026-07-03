package musicplaylistshuffler.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeUtilsTest {

    @Test
    void formatDuration_underOneMinute() {
        assertEquals("0:45", TimeUtils.formatDuration(45));
    }

    @Test
    void formatDuration_exactlyOneMinute() {
        assertEquals("1:00", TimeUtils.formatDuration(60));
    }

    @Test
    void formatDuration_minutesAndSeconds() {
        assertEquals("2:05", TimeUtils.formatDuration(125));
    }

    @Test
    void formatDuration_exactlyOneHour() {
        assertEquals("1:00:00", TimeUtils.formatDuration(3600));
    }

    @Test
    void formatDuration_hoursMinutesSeconds() {
        assertEquals("1:01:05", TimeUtils.formatDuration(3665));
    }

    @Test
    void formatDuration_zeroSeconds() {
        assertEquals("0:00", TimeUtils.formatDuration(0));
    }

    @Test
    void formatDuration_multipleHours() {
        assertEquals("2:30:15", TimeUtils.formatDuration(9015));
    }
}