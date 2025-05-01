package com.dailyreader.daily_reader.exception;

import java.util.function.Supplier;

public class ExceptionHandler {

    public static void throwIf(boolean condition, Supplier<RuntimeException> exceptionSupplier) {
        if (condition) {
            throw exceptionSupplier.get();
        }
    }
}
