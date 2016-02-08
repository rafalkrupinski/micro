package com.hashnot.u.range;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

import java.time.Duration;
import java.time.temporal.Temporal;

/**
 * @author Rafał Krupiński
 */
public class Ranges {
    private Ranges() {
        throw new RuntimeException("Util class");
    }

    public static <ComparableT extends Comparable<ComparableT>, TemporalT extends Temporal & Comparable<ComparableT>> Duration between(Range<TemporalT> timeRange) {
        return Duration.between(timeRange.lowerEndpoint(), timeRange.upperEndpoint());
    }

    public static <T extends Comparable<T>> boolean isSingleton(Range<T> range) {
        return range.upperBoundType() == BoundType.CLOSED
                && range.lowerBoundType() == BoundType.CLOSED
                && range.lowerEndpoint().equals(range.upperEndpoint());
    }
}
