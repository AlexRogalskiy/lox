package com.caco3.lox.environment;

import com.caco3.lox.LoxException;
import com.caco3.lox.util.Assert;

public class NoSuchVariableException extends LoxException {
    private final String variableName;

    private NoSuchVariableException(String variableName, String message) {
        super(message);
        Assert.notNull(variableName, "variableName == null");

        this.variableName = variableName;
    }

    public static NoSuchVariableException forVariableName(String variableName) {
        Assert.notNull(variableName, "variableName == null");

        return new NoSuchVariableException(variableName, "variableName = '" + variableName + "'");
    }
}
