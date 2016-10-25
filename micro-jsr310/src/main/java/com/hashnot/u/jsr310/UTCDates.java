package com.hashnot.u.jsr310;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;

/**
 * JSR310 date &amp; time conversion utils that use the UTC default time zone.
 * @author Rafał Krupiński
 */
public class UTCDates {
    /**
     * Convert {@link Instant} to {@link LocalDate} discarding time information, using the UTC zone id.
     */
    public static LocalDate toLocalDate(Instant instant) {
        return Dates.toLocalDate(instant, UTC);
    }

    /**
     * Convert {@link LocalDateTime} to {@link Instant} with the UTC zone id.
     */
    public static Instant toInstant(LocalDateTime time) {
        return Dates.toInstant(time, UTC);
    }

    /**
     * Convert {@link LocalDate} at the beginning of day to {@link Instant} at the UTC zone id.
     */
    public static Instant toInstant(LocalDate date) {
        return Dates.toInstant(date, UTC);
    }

}
