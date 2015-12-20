package com.hashnot.u.range;

import java.util.Comparator;

/**
 * @author Rafał Krupiński
 */
public class RangeOfComparables<ComparableType extends Comparable<ComparableType>> extends Range<ComparableType> implements Comparable<Range<ComparableType>> {
    private final Comparator<Range<ComparableType>> rangeComparator;

    public RangeOfComparables(ComparableType bottom, ComparableType top, Comparator<Range<ComparableType>> rangeComparator) {
        super(bottom, top);
        this.rangeComparator = rangeComparator;
    }

    public static <T extends Comparable<T>> Range<T> point(T value, Comparator<Range<T>> rangeComparator) {
        return new RangeOfComparables<>(value, value, rangeComparator);
    }

    public RangeOfComparables<ComparableType> withBottom(ComparableType bottom) {
        return new RangeOfComparables<>(bottom, top, rangeComparator);
    }

    @Override
    public RangeOfComparables<ComparableType> withTop(ComparableType top) {
        return new RangeOfComparables<>(bottom, top, rangeComparator);
    }

    @Override
    public int compareTo(Range<ComparableType> o) {
        return rangeComparator.compare(this, o);
    }
}
