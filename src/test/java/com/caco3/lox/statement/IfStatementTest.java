package com.caco3.lox.statement;

import com.caco3.lox.expression.LiteralExpression;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.statement.visitor.TrackingStatementVisitor;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class IfStatementTest {

    @Test
    void acceptImplementedCorrectly() {
        IfStatement ifStatement = IfStatement.of(
                Token.of("if", 1, Token.Type.IF),
                LiteralExpression.of(Token.of("1", 1, Token.Type.NUMBER_LITERAL)),
                BlockStatement.of(
                        Token.of("{", 1, Token.Type.LEFT_BRACKET),
                        Collections.emptyList(),
                        Token.of("}", 1, Token.Type.RIGHT_BRACKET)
                ),
                null
        );
        TrackingStatementVisitor visitor = new TrackingStatementVisitor();

        ifStatement.accept(visitor);

        assertThat(visitor.getIfStatements())
                .containsExactly(ifStatement);
    }
}