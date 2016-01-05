package com.hashnot.u.range;

import com.google.common.collect.Range;

import java.time.Duration;
import java.time.temporal.Temporal;

/**
 * @author Rafał Krupiński
 */
public class Ranges {
    protected Ranges() {
    }

    public static <ComparableT extends Comparable<ComparableT>, TemporalT extends Temporal & Comparable<ComparableT>> Duration between(Range<TemporalT> timeRange) {
        return Duration.between(timeRange.lowerEndpoint(), timeRange.upperEndpoint());
    }
}
