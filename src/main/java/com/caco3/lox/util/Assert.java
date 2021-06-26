package com.caco3.lox.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@SuppressWarnings("AbstractClassWithoutAbstractMethods")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Assert {
    public static void notNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object o, Supplier<String> messageSupplier) {
        if (o == null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    @SuppressWarnings("BooleanParameter")
    public static void isTrue(boolean condition, Supplier<String> messageSupplier) {
        if (condition) {
            return;
        }
        throw new IllegalArgumentException(nullSafeGet(messageSupplier));
    }

    @SuppressWarnings("BooleanParameter")
    public static void state(boolean state, Supplier<String> messageSupplier) {
        if (state) {
            return;
        }

        throw new IllegalStateException(nullSafeGet(messageSupplier));
    }

    private static <T> T nullSafeGet(Supplier<? extends T> supplier) {
        if (supplier == null) {
            return null;
        }
        return supplier.get();
    }
}
