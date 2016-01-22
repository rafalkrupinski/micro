package com.hashnot.u.compare;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Comparator;

import static org.junit.Assert.*;

/**
 * @author Rafał Krupiński
 */
public class BigDecimapCompareOpTest {

    @Test
    public void testNe() throws Exception {
        CompareOp<BigDecimal> cmp = CompareOp.<BigDecimal>from(Comparator.naturalOrder());
        BigDecimal myZero = new BigDecimal("0.00");
        assertFalse(cmp.ne(myZero, BigDecimal.ZERO));
    }
}