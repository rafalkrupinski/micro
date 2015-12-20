package com.hashnot.u.range;

import java.util.Comparator;

/**
 * @author Rafał Krupiński
 */
public class NonOverlappingRangeComparator<ComparableType extends Comparable<ComparableType>> implements Comparator<Range<ComparableType>> {
    private static final String OVERLAPPING_RANGES = "Overlapping ranges";
    private static final String ILLEGAL_RANGE = "Illegal range";

    private static final NonOverlappingRangeComparator INST = new NonOverlappingRangeComparator<>();

    public static <T extends Comparable<T>> Comparator<Range<T>> comparator() {
        return INST;
    }

    @Override
    public int compare(Range<ComparableType> a, Range<ComparableType> b) {
        assertNotNull(a);
        assertNotNull(b);
        assertFinite(a);
        assertFinite(b);

        assertValidRange(a);
        assertValidRange(b);

        checkInfinity(a, b);

        int bd = compare(a.getBottom(), b.getBottom(), -1);
        if (bd == 0)
            throw new IllegalArgumentException(OVERLAPPING_RANGES);

        int td = compare(a.getTop(), b.getTop(), 1);
        if (td == 0)
            throw new IllegalArgumentException(OVERLAPPING_RANGES);

        return bd;
    }

    private void assertNotNull(Range<ComparableType> r) {
        if (r == null)
            throw new NullPointerException();
    }

    private static <ComparableType> void checkInfinity(Range<ComparableType> a, Range<ComparableType> b) {
        if ((a.getBottom() == null && b.getBottom() == null)
                || (a.getTop() == null && b.getTop() == null))
            throw new IllegalArgumentException(OVERLAPPING_RANGES);
    }

    private static <T extends Comparable<T>> void assertValidRange(Range<T> r) {
        if (r.getBottom() != null && r.getTop() != null && r.getBottom().compareTo(r.getTop()) > 0)
            throw new IllegalArgumentException(ILLEGAL_RANGE);
    }

    private static <ComparableType> void assertFinite(Range<ComparableType> r) {
        if (r.getBottom() == null && r.getTop() == null)
            throw new IllegalArgumentException(OVERLAPPING_RANGES);
    }

    /***
     * @param nullValSig signum of assumed null value
     */
    private static <T extends Comparable<T>> int compare(T a, T b, int nullValSig) {
        if (a == b) return 0;
        if (a == null)
            return nullValSig;
        else if (b == null)
            return -nullValSig;
        else return a.compareTo(b);
    }
}
