package com.hashnot.u.time;

import java.time.Duration;

/**
 * @author Rafał Krupiński
 */
public class DurationUtil {

    public static final double NANOS_PER_SECOND_DBL = 1_000_000_000;

    public static Double toSecondsDouble(Duration duration) {
        return duration == null ? null : duration.getSeconds() + duration.getNano() / NANOS_PER_SECOND_DBL;
    }

}
