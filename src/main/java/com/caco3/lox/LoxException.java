package com.caco3.lox;

@SuppressWarnings("AbstractClassWithoutAbstractMethods")
public abstract class LoxException extends RuntimeException {
    public LoxException() {
    }

    public LoxException(String message) {
        super(message);
    }

    public LoxException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoxException(Throwable cause) {
        super(cause);
    }
}
