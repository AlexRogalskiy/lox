package com.caco3.lox.interpreter;

import com.caco3.lox.lexer.DefaultLexer;
import com.caco3.lox.lexer.Lexer;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.parser.DefaultParser;
import com.caco3.lox.parser.Parser;
import com.caco3.lox.statement.Statement;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class InterpreterVisitorTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream printStream = new PrintStream(outputStream);
    private final InterpreterVisitor interpreterVisitor = InterpreterVisitor.of(printStream);

    private String getOutput() {
        printStream.flush();
        return normalize(outputStream.toString(StandardCharsets.UTF_8));
    }

    private String normalize(String string) {
        return string.replace("\r\n", "\n");
    }

    @ParameterizedTest
    @MethodSource(value = "testInputs")
    void interpretationTest(String input, String expectedOutput) {
        Lexer lexer = new DefaultLexer(input);
        List<Token> tokens = lexer.parseTokens();
        Parser parser = new DefaultParser(tokens);
        List<Statement> statements = parser.parseStatements();

        statements.forEach(it -> it.accept(interpreterVisitor));

        String actualOutput = getOutput();

        assertThat(actualOutput)
                .isEqualTo(expectedOutput);
    }

    public static Stream<Arguments> testInputs() {
        return Stream.of(
                Arguments.of("print 123;", "123"),
                Arguments.of("print 1 + 3 * 5 - 3;", "13"),
                Arguments.of("print 3 + 13 / 2;", "9"),
                Arguments.of("print -5;", "-5"),
                Arguments.of("print \"abc\" + \" \" + \"def\";", "abc def"),
                Arguments.of("print 1 > 2;", "false"),
                Arguments.of("print !(1 > 2);", "true"),
                Arguments.of("print 1 == 2;", "false"),
                Arguments.of("print 1 != 2;", "true"),
                Arguments.of("print 1 >= 2;", "false"),
                Arguments.of("print 1 < 2;", "true"),
                Arguments.of("print 1 <= 2;", "true"),
                Arguments.of("var x = 10 * 2; print x;", "20"),
                Arguments.of("var x = 10 * 2; print x; { var x = 42; print x; }", "2042"),
                Arguments.of("var x = 10 * 2; print x; { var x = 42; print x; } print x;", "204220"),
                Arguments.of("1 + 2;", ""),
                Arguments.of("var x = 10; x = 20; print x;", "20"),
                Arguments.of("if (5 < 7) print \"5 < 7\"; else print \"5 >= 7\";", "5 < 7"),
                Arguments.of("if (5 > 7) print 1; else print 2;", "2"),
                Arguments.of("for (var i = 0; i < 10; i = i + 1) { print i; }", "0123456789"),
                Arguments.of("println(\"abc\");", "abc\n"),
                Arguments.of("function thrice(a) { return a * 3; } println(thrice(10));", "30\n"),
                Arguments.of("function fibonacciOf(a) { if (a <= 2) return 1; else return fibonacciOf(a - 2) + fibonacciOf(a - 1); } println(fibonacciOf(1));", "1\n"),
                Arguments.of("function fibonacciOf(a) { if (a <= 2) return 1; else return fibonacciOf(a - 2) + fibonacciOf(a - 1); } println(fibonacciOf(2));", "1\n"),
                Arguments.of("function fibonacciOf(a) { if (a <= 2) return 1; else return fibonacciOf(a - 2) + fibonacciOf(a - 1); } println(fibonacciOf(10));", "55\n"),
                Arguments.of("function fibonacciOf(a) { \n" +
                             "    if (a <= 2) \n" +
                             "        return 1; \n" +
                             "    else \n" +
                             "        return fibonacciOf(a - 2) + fibonacciOf(a - 1);\n" +
                             "}\n" +
                             "println(fibonacciOf(3));\n", "2\n"),
                Arguments.of("var x = 0; while (x < 10) { print x; x = x + 1; }", "0123456789")
        );
    }
}