package com.caco3.lox.parser;

import com.caco3.lox.expression.AssignmentExpression;
import com.caco3.lox.expression.BinaryExpression;
import com.caco3.lox.expression.IdentifierExpression;
import com.caco3.lox.expression.LiteralExpression;
import com.caco3.lox.lexer.DefaultLexer;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.statement.BlockStatement;
import com.caco3.lox.statement.ExpressionStatement;
import com.caco3.lox.statement.IfStatement;
import com.caco3.lox.statement.PrintStatement;
import com.caco3.lox.statement.Statement;
import com.caco3.lox.statement.VariableDeclarationStatement;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultParserTest {
    @Test
    void simplePrintStatementParsed() {
        Token printToken = Token.of("print", 1, Token.Type.PRINT);
        Token stringToPrint = Token.of("def", 1, Token.Type.STRING_LITERAL);
        Token semicolon = Token.of(";", 1, Token.Type.SEMICOLON);
        Parser parser = newParser(
                printToken,
                stringToPrint,
                semicolon
        );

        List<Statement> statements = parser.parseStatements();

        assertThat(statements)
                .containsExactly(
                        PrintStatement.of(printToken, LiteralExpression.of(stringToPrint))
                );
    }

    @Test
    void printWithSimpleArithmeticExpressionIsParsed() {
        Token print = Token.of("print", 1, Token.Type.PRINT);
        Token one = Token.of("1", 1, Token.Type.NUMBER_LITERAL);
        Token plus = Token.of("+", 1, Token.Type.PLUS);
        Token five = Token.of("5", 1, Token.Type.NUMBER_LITERAL);
        Token star = Token.of("*", 1, Token.Type.STAR);
        Token two = Token.of("2", 1, Token.Type.NUMBER_LITERAL);
        Token semicolon = Token.of(";", 1, Token.Type.SEMICOLON);

        Parser parser = newParser(print, one, plus, five, star, two, semicolon);

        List<Statement> statements = parser.parseStatements();

        assertThat(statements)
                .containsExactly(
                        PrintStatement.of(
                                print,
                                BinaryExpression.of(
                                        LiteralExpression.of(one),
                                        BinaryExpression.of(LiteralExpression.of(five), LiteralExpression.of(two), star),
                                        plus
                                )
                        )
                );
    }

    @Test
    void printSimpleStringParsed() {
        Token print = Token.of("print", 1, Token.Type.PRINT);
        Token string = Token.of("Hello", 1, Token.Type.STRING_LITERAL);
        Token semicolon = Token.of(";", 1, Token.Type.SEMICOLON);
        Parser parser = newParser(print, string, semicolon);

        List<Statement> statements = parser.parseStatements();

        assertThat(statements)
                .containsExactly(
                        PrintStatement.of(print, LiteralExpression.of(string))
                );
    }

    @Test
    void printWithConcatenatedStringParsed() {
        Token print = Token.of("print", 1, Token.Type.PRINT);
        Token firstString = Token.of("Cosmic", 1, Token.Type.STRING_LITERAL);
        Token firstPlus = Token.of("+", 1, Token.Type.PLUS);
        Token secondString = Token.of(" ", 1, Token.Type.STRING_LITERAL);
        Token secondPlus = Token.of("+", 1, Token.Type.PLUS);
        Token thirdString = Token.of("Gate", 1, Token.Type.STRING_LITERAL);
        Token semicolon = Token.of(";", 1, Token.Type.SEMICOLON);
        Parser parser = newParser(print, firstString, firstPlus, secondString, secondPlus, thirdString, semicolon);

        List<Statement> statements = parser.parseStatements();

        assertThat(statements)
                .containsExactly(
                        PrintStatement.of(print,
                                BinaryExpression.of(
                                        BinaryExpression.of(LiteralExpression.of(firstString), LiteralExpression.of(secondString), firstPlus),
                                        LiteralExpression.of(thirdString),
                                        secondPlus
                                )
                        )
                );
    }

    @Test
    void arithmeticExpressionWithMixedPrecedenceOperatorsParsed() {
        // print 2 * 3 + 1;
        Token print = Token.of("print", 1, Token.Type.PRINT);
        Token two = Token.of("2", 1, Token.Type.NUMBER_LITERAL);
        Token star = Token.of("*", 1, Token.Type.STAR);
        Token three = Token.of("3", 1, Token.Type.NUMBER_LITERAL);
        Token plus = Token.of("+", 1, Token.Type.PLUS);
        Token one = Token.of("1", 1, Token.Type.NUMBER_LITERAL);
        Token semicolon = Token.of(";", 1, Token.Type.SEMICOLON);
        Parser parser = newParser(print, two, star, three, plus, one, semicolon);

        List<Statement> statements = parser.parseStatements();

        assertThat(statements)
                .containsExactly(
                        PrintStatement.of(
                                print,
                                BinaryExpression.of(
                                        BinaryExpression.of(LiteralExpression.of(two), LiteralExpression.of(three), star),
                                        LiteralExpression.of(one),
                                        plus
                                )
                        )
                );
    }

    @Test
    void blockSuccessfullyParsed() {
        Token leftBracket = Token.of("{", 1, Token.Type.LEFT_BRACKET);
        Token var = Token.of("var", 2, Token.Type.VAR);
        Token x = Token.of("x", 2, Token.Type.IDENTIFIER);
        Token equal = Token.of("=", 2, Token.Type.EQUAL);
        Token ten = Token.of("10", 2, Token.Type.NUMBER_LITERAL);
        Token semicolon = Token.of(";", 2, Token.Type.SEMICOLON);
        Token rightBracket = Token.of("}", 3, Token.Type.RIGHT_BRACKET);
        Parser parser = newParser(leftBracket, var, x, equal, ten, semicolon, rightBracket);

        List<Statement> statements = parser.parseStatements();

        assertThat(statements)
                .containsExactly(
                        BlockStatement.of(
                                leftBracket,
                                List.of(
                                        VariableDeclarationStatement.of(
                                                x, LiteralExpression.of(ten)
                                        )
                                ),
                                rightBracket
                        )
                );
    }

    @Test
    void expressionStatementParsed() {
        Token one = Token.of("1", 1, Token.Type.NUMBER_LITERAL);
        Token plus = Token.of("+", 1, Token.Type.PLUS);
        Token two = Token.of("2", 1, Token.Type.NUMBER_LITERAL);
        Token semicolon = Token.of(";", 1, Token.Type.SEMICOLON);
        Parser parser = newParser(one, plus, two, semicolon);

        List<Statement> statements = parser.parseStatements();

        assertThat(statements)
                .containsExactly(
                        ExpressionStatement.of(
                                BinaryExpression.of(
                                        LiteralExpression.of(one),
                                        LiteralExpression.of(two),
                                        plus
                                ),
                                semicolon
                        )
                );
    }

    @Test
    void assignmentParsed() {
        Token x = Token.of("x", 1, Token.Type.IDENTIFIER);
        Token equal = Token.of("=", 1, Token.Type.EQUAL);
        Token ten = Token.of("10", 1, Token.Type.NUMBER_LITERAL);
        Token semicolon = Token.of(";", 1, Token.Type.SEMICOLON);

        Parser parser = newParser(x, equal, ten, semicolon);
        List<Statement> statements = parser.parseStatements();

        assertThat(statements)
                .containsExactly(
                        ExpressionStatement.of(
                                AssignmentExpression.of(x, equal, LiteralExpression.of(ten)),
                                semicolon
                        )
                );

    }

    @Test
    void ifElseParsed() {
        List<Token> tokens = new DefaultLexer("if (a > b) if (c > d) 1; else 2;").parseTokens();
        Parser parser = new DefaultParser(tokens);
        List<Statement> statements = parser.parseStatements();

        assertThat(statements)
                .containsExactly(
                        IfStatement.of(
                                Token.of("if", 1, Token.Type.IF),
                                BinaryExpression.of(
                                        IdentifierExpression.of(Token.of("a", 1, Token.Type.IDENTIFIER)),
                                        IdentifierExpression.of(Token.of("b", 1, Token.Type.IDENTIFIER)),
                                        Token.of(">", 1, Token.Type.GREATER)
                                ),
                                IfStatement.of(
                                        Token.of("if", 1, Token.Type.IF),
                                        BinaryExpression.of(
                                                IdentifierExpression.of(Token.of("c", 1, Token.Type.IDENTIFIER)),
                                                IdentifierExpression.of(Token.of("d", 1, Token.Type.IDENTIFIER)),
                                                Token.of(">", 1, Token.Type.GREATER)
                                        ),
                                        ExpressionStatement.of(
                                                LiteralExpression.of(Token.of("1", 1, Token.Type.NUMBER_LITERAL)),
                                                Token.of(";", 1, Token.Type.SEMICOLON)
                                        ),
                                        ExpressionStatement.of(
                                                LiteralExpression.of(Token.of("2", 1, Token.Type.NUMBER_LITERAL)),
                                                Token.of(";", 1, Token.Type.SEMICOLON)
                                        )
                                ),
                                null
                        )
                );
    }

    private static Parser newParser(Token... tokens) {
        return new DefaultParser(Arrays.asList(tokens));
    }
}