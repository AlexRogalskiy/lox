package com.caco3.lox.function;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PrintlnFunctionTest {
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final PrintStream printStream = new PrintStream(byteArrayOutputStream);
    private final PrintlnFunction printlnFunction = new PrintlnFunction(printStream);

    private String getOutput() {
        printStream.flush();
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8)
                .replace("\r\n", "\n");
    }

    @Test
    void printlnPrintsToPrintStream() {
        printlnFunction.invoke(List.of("abcdef"));

        assertThat(getOutput())
                .isEqualTo("abcdef\n");
    }
}