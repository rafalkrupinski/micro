package com.hashnot.u.random;

import com.google.common.collect.Range;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class WeightedRandom<T> {
    private final Random rnd;
    private final TreeMap<Range<Double>, T> ranges = new TreeMap<>();

    public WeightedRandom(Map<T, Double> weightedItems) {
        this(weightedItems, new Random());
    }

    public WeightedRandom(Map<T, Double> weightedItems, Random random) {
        Double bottom = 0d;

        Map<T, Double> normalized = normalize(weightedItems);

        for (Map.Entry<T, Double> wi : normalized.entrySet()) {
            double weight = wi.getValue();
            if (weight > 0) {
                Double top = bottom + weight;
                Range<Double> r = Range.closedOpen(bottom, top);
                if (ranges.containsKey(r)) {
                    T other = ranges.get(r);
                    throw new IllegalArgumentException(String.format("Range %s conflicts with range %s", r, other));
                }
                ranges.put(r, wi.getKey());
                bottom = top;
            }
        }
        rnd = random;
    }

    protected Map<T, Double> normalize(Map<T, Double> weightedItems) {
        Map<T, Double> normalized = new HashMap<>();

        double sum = 0d;

        for (Map.Entry<T, Double> e : weightedItems.entrySet()) {
            Double weight = e.getValue();
            sum += weight;
        }

        for (Map.Entry<T, Double> e : weightedItems.entrySet()) {
            normalized.put(e.getKey(), e.getValue() / sum);
        }
        return normalized;
    }

    public T next() {
        double key = rnd.nextDouble();

        Range<Double> range = Range.singleton(key);
        return ranges.get(range);
    }
}
