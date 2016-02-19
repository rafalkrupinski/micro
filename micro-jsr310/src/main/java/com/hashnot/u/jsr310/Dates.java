package com.hashnot.u.jsr310;

import java.time.*;

import static java.time.ZoneId.systemDefault;

/**
 * @author Rafał Krupiński
 */
public class Dates {
    private Dates() {
    }

    public static LocalDate toLocalDate(Instant instant) {
        return toLocalDate(instant, systemDefault());
    }

    public static LocalDate toLocalDate(Instant instant, ZoneId zone) {
        return ZonedDateTime.ofInstant(instant, zone).toLocalDate();
    }

    public static Instant toInstant(LocalDateTime time, ZoneId zone) {
        return time.atZone(zone).toInstant();
    }

    public static Instant toInstant(LocalDateTime time) {
        return toInstant(time, systemDefault());
    }

    public static Instant toInstant(LocalDate date, ZoneId zone) {
        return date.atStartOfDay().atZone(zone).toInstant();
    }

    public static Instant toInstant(LocalDate date) {
        return toInstant(date, systemDefault());
    }
}
