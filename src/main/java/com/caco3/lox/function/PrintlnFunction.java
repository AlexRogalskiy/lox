package com.caco3.lox.function;

import com.caco3.lox.util.Assert;
import lombok.AllArgsConstructor;

import java.io.PrintStream;
import java.util.List;

@AllArgsConstructor
public class PrintlnFunction implements Invocable {
    private final PrintStream printStream;

    @Override
    public Object invoke(List<Object> arguments) {
        Assert.notNull(arguments, "arguments == null");
        Assert.isTrue(arguments.size() == 1, () -> "Too many arguments = " + arguments);

        printStream.println(arguments.get(0));
        return null;
    }
}
