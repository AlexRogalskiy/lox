package com.caco3.lox.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@SuppressWarnings("AbstractClassWithoutAbstractMethods")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Assert {
    public static void notNull(Object o, Supplier<String> messageSupplier) {
        if (o == null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    private static <T> T nullSafeGet(Supplier<? extends T> supplier) {
        if (supplier == null) {
            return null;
        }
        return supplier.get();
    }
}
