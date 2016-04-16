package com.hashnot.u.jsr310;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.ZoneId.systemDefault;

/**
 * JSR310 date & time conversion utils that use the system system default time zone.
 * @author Rafał Krupiński
 */
public class SystemDates {
    /**
     * Convert {@link Instant} to {@lonk LocalDate} discarding time information, using the system default time zone.
     */
    public static LocalDate toLocalDate(Instant instant) {
        return Dates.toLocalDate(instant, systemDefault());
    }

    /**
     * Convert {@lonk LocalDateTime} to {@link Instant} with the system default time zone.
     */
    public static Instant toInstant(LocalDateTime time) {
        return Dates.toInstant(time, systemDefault());
    }

    /**
     * Convert {@link LocalDate} at the beginning of day to {@link Instant} at the system default time zone.
     */
    public static Instant toInstant(LocalDate date) {
        return Dates.toInstant(date, systemDefault());
    }
}
