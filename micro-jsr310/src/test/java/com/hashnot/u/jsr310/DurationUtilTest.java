package com.hashnot.u.jsr310;

import com.hashnot.u.jsr310.DurationUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @author Rafał Krupiński
 */
public class DurationUtilTest {
    private static final double DELTA = 1e-15;

    @Test
    public void testToDouble() throws Exception {
        Duration duration = Duration.of(1001000000, ChronoUnit.NANOS);
        Double dblDuration = DurationUtil.toSecondsDouble(duration);
        double expected = 1.001;
        Assert.assertEquals(expected, dblDuration, DELTA);
    }
}
