package com.caco3.lox.function;

import java.util.List;

public interface Invocable {
    Object invoke(List<Object> arguments);
}
