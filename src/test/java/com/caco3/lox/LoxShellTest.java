package com.caco3.lox;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

class LoxShellTest {
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final PrintStream printStream = new PrintStream(byteArrayOutputStream);

    private String getOutput() {
        printStream.flush();
        String string = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        return string.replace("\r\n", "\n");
    }

    @Test
    void helloWorldPrinted() {
        String input = """
                println("Hello world");
                """;

        LoxShell loxShell = new LoxShell(printStream, new Scanner(input));
        loxShell.run();

        assertThat(getOutput())
                .isEqualTo("Hello world\n");
    }
}