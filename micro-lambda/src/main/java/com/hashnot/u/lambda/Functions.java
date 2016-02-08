package com.hashnot.u.lambda;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Rafał Krupiński
 */
public class Functions {
    public static <InputT, OutputT> Function<InputT, OutputT> always(OutputT value) {
        return k -> value;
    }

    public static <InputT, OutputT> Function<InputT, OutputT> from(Supplier<OutputT> supplier) {
        return k -> supplier.get();
    }
}
