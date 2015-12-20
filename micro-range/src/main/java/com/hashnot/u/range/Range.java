package com.hashnot.u.range;

import java.util.Map;

/**
 * @author Rafał Krupiński
 */
public class Range<T> {
    protected final T bottom;
    protected final T top;

    public Range(T bottom, T top) {
        this.top = top;
        this.bottom = bottom;
    }

    public static <T> Range<T> point(T value) {
        return new Range<>(value, value);
    }

    public T getBottom() {
        return bottom;
    }

    public T getTop() {
        return top;
    }

    public Range<T> withBottom(T bottom) {
        return new Range<>(bottom, top);
    }

    public Range<T> withTop(T top) {
        return new Range<>(bottom, top);
    }

    @Override
    public String toString() {
        return "<" + bottom + ", " + top + '>';
    }

    public Map.Entry<T, T> asEntry() {
        return new Map.Entry<T, T>() {
            @Override
            public T getKey() {
                return bottom;
            }

            @Override
            public T getValue() {
                return top;
            }

            @Override
            public T setValue(T value) {
                throw new UnsupportedOperationException("Setting value is not supported");
            }
        };
    }
}
