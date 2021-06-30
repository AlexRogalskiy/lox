package com.caco3.lox.lexer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LexerTest {
    @Test
    void simpleExpressionParsed() {
        Lexer lexer = newLexer("()");
        List<Token> tokens = lexer.parseTokens();

        assertThat(tokens)
                .containsExactly(
                        Token.of("(", 1, Token.Type.LEFT_PARENTHESIS),
                        Token.of(")", 1, Token.Type.RIGHT_PARENTHESIS)
                );
    }

    @Test
    void numberLiteralIsParsed() {
        Lexer lexer = newLexer("12345");

        List<Token> tokens = lexer.parseTokens();
        assertThat(tokens)
                .containsExactly(
                        Token.of("12345", 1, Token.Type.NUMBER_LITERAL)
                );
    }

    @Test
    void arithmeticExpressionIsParsed() {
        Lexer lexer = newLexer("1 + 2 / 3 * 9 - 13");
        List<Token> tokens = lexer.parseTokens();

        assertThat(tokens)
                .containsExactly(
                        Token.of("1", 1, Token.Type.NUMBER_LITERAL),
                        Token.of("+", 1, Token.Type.PLUS),
                        Token.of("2", 1, Token.Type.NUMBER_LITERAL),
                        Token.of("/", 1, Token.Type.SLASH),
                        Token.of("3", 1, Token.Type.NUMBER_LITERAL),
                        Token.of("*", 1, Token.Type.STAR),
                        Token.of("9", 1, Token.Type.NUMBER_LITERAL),
                        Token.of("-", 1, Token.Type.MINUS),
                        Token.of("13", 1, Token.Type.NUMBER_LITERAL)
                );
    }

    @Test
    void multilineExpressionIsParsed() {
        Lexer lexer = newLexer("1\n2");

        List<Token> tokens = lexer.parseTokens();

        assertThat(tokens)
                .containsExactly(
                        Token.of("1", 1, Token.Type.NUMBER_LITERAL),
                        Token.of("2", 2, Token.Type.NUMBER_LITERAL)
                );
    }

    @Test
    void ifElseParsed() {
        Lexer lexer = newLexer("if (1 > 2) { } else { }");
        List<Token> tokens = lexer.parseTokens();
        assertThat(tokens)
                .containsExactly(
                        Token.of("if", 1, Token.Type.IF),
                        Token.of("(", 1, Token.Type.LEFT_PARENTHESIS),
                        Token.of("1", 1, Token.Type.NUMBER_LITERAL),
                        Token.of(">", 1, Token.Type.GREATER),
                        Token.of("2", 1, Token.Type.NUMBER_LITERAL),
                        Token.of(")", 1, Token.Type.RIGHT_PARENTHESIS),
                        Token.of("{", 1, Token.Type.LEFT_BRACKET),
                        Token.of("}", 1, Token.Type.RIGHT_BRACKET),
                        Token.of("else", 1, Token.Type.ELSE),
                        Token.of("{", 1, Token.Type.LEFT_BRACKET),
                        Token.of("}", 1, Token.Type.RIGHT_BRACKET)
                );
    }

    @Test
    void variableDeclarationAndInitializationParsed() {
        Lexer lexer = newLexer("var x = abc");
        List<Token> tokens = lexer.parseTokens();

        assertThat(tokens)
                .containsExactly(
                        Token.of("var", 1, Token.Type.VAR),
                        Token.of("x", 1, Token.Type.IDENTIFIER),
                        Token.of("=", 1, Token.Type.EQUAL),
                        Token.of("abc", 1, Token.Type.IDENTIFIER)
                );
    }

    @Test
    void comparisonOperatorsAreParsed() {
        Lexer lexer = newLexer("<= < == != > >=");
        List<Token> tokens = lexer.parseTokens();

        assertThat(tokens)
                .containsExactly(
                        Token.of("<=", 1, Token.Type.LESS_EQUAL),
                        Token.of("<", 1, Token.Type.LESS),
                        Token.of("==", 1, Token.Type.EQUAL_EQUAL),
                        Token.of("!=", 1, Token.Type.BANG_EQUAL),
                        Token.of(">", 1, Token.Type.GREATER),
                        Token.of(">=", 1, Token.Type.GREATER_EQUAL)
                );
    }

    @Test
    void stringLiteralIsParsed() {
        Lexer lexer = newLexer("\"abc\"");
        List<Token> tokens = lexer.parseTokens();

        assertThat(tokens)
                .containsExactly(
                        Token.of("abc", 1, Token.Type.STRING_LITERAL)
                );
    }

    @Test
    void printParsed() {
        Lexer lexer = newLexer("print \"abc\";");
        List<Token> tokens = lexer.parseTokens();

        assertThat(tokens)
                .containsExactly(
                        Token.of("print", 1, Token.Type.PRINT),
                        Token.of("abc", 1, Token.Type.STRING_LITERAL),
                        Token.of(";", 1, Token.Type.SEMICOLON)
                );
    }

    @Test
    void functionDeclarationParsed() {
        Lexer lexer = newLexer("function foo(a, b)");

        List<Token> tokens = lexer.parseTokens();

        assertThat(tokens)
                .containsExactly(
                        Token.of("function", 1, Token.Type.FUNCTION),
                        Token.of("foo", 1, Token.Type.IDENTIFIER),
                        Token.of("(", 1, Token.Type.LEFT_PARENTHESIS),
                        Token.of("a", 1, Token.Type.IDENTIFIER),
                        Token.of(",", 1, Token.Type.COMMA),
                        Token.of("b", 1, Token.Type.IDENTIFIER),
                        Token.of(")", 1, Token.Type.RIGHT_PARENTHESIS)
                );
    }

    private static Lexer newLexer(String s) {
        return new DefaultLexer(s);
    }
}
