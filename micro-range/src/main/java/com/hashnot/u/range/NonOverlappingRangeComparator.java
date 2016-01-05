package com.hashnot.u.range;

import com.google.common.collect.Range;

import java.util.Comparator;

/**
 * @author Rafał Krupiński
 */
public class NonOverlappingRangeComparator<ComparableType extends Comparable<ComparableType>> implements Comparator<Range<ComparableType>> {
    private static final String OVERLAPPING_RANGES = "Overlapping ranges";

    private static final NonOverlappingRangeComparator INST = new NonOverlappingRangeComparator<>();

    public static <T extends Comparable<T>> Comparator<Range<T>> comparator() {
        return INST;
    }

    @Override
    public int compare(Range<ComparableType> a, Range<ComparableType> b) {
        assertNotNull(a);
        assertNotNull(b);

        if (a.upperEndpoint().equals(b.upperEndpoint())
                && a.upperBoundType().equals(b.upperBoundType())
                && a.lowerEndpoint() == b.lowerEndpoint()
                && a.lowerBoundType() == b.lowerBoundType())
            return 0;

        if (a.isConnected(b))
            throw new IllegalArgumentException(OVERLAPPING_RANGES);

        return a.lowerEndpoint().compareTo(b.lowerEndpoint());
    }

    private void assertNotNull(Range<ComparableType> r) {
        if (r == null)
            throw new NullPointerException();
    }
}
