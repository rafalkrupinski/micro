package com.hashnot.u.jsr310;

import java.time.*;

/**
 * JSR310 date &amp; time conversion utils
 * @author Rafał Krupiński
 */
public class Dates {
    private Dates() {
    }

    /**
     * Convert {@link Instant} to {@link LocalDate} at a given time zone, discarding time information
     */
    public static LocalDate toLocalDate(Instant instant, ZoneId zone) {
        return ZonedDateTime.ofInstant(instant, zone).toLocalDate();
    }

    /**
     * Convert {@link LocalDateTime} to {@link Instant} with the given time zone.
     */
    public static Instant toInstant(LocalDateTime time, ZoneId zone) {
        return time.atZone(zone).toInstant();
    }

    /**
     * Convert {@link LocalDate} at the beginning of day to {@link Instant} at the given time zone.
     */
    public static Instant toInstant(LocalDate date, ZoneId zone) {
        return date.atStartOfDay().atZone(zone).toInstant();
    }
}
