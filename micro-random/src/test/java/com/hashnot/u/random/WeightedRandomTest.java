package com.hashnot.u.random;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Rafał Krupiński
 */
public class WeightedRandomTest {
    @Test
    public void next() throws Exception {
        Map<String, Double> data = new HashMap<>();
        data.put("apple", 1d);
        data.put("orange", 10d);
        WeightedRandom<String> weightedRandom = new WeightedRandom<>(data);
        String next = weightedRandom.next();
        assertTrue("apple".equals(next) || "orange".equals(next));
    }

}