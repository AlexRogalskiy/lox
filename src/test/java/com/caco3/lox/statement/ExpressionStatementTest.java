package com.caco3.lox.statement;

import com.caco3.lox.expression.LiteralExpression;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.statement.visitor.TrackingStatementVisitor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExpressionStatementTest {
    @Test
    void visitImplemented() {
        TrackingStatementVisitor trackingStatementVisitor = new TrackingStatementVisitor();
        ExpressionStatement expressionStatement = ExpressionStatement.of(
                LiteralExpression.of(
                        Token.of("1", 1, Token.Type.NUMBER_LITERAL)
                ),
                Token.of(";", 1, Token.Type.SEMICOLON)
        );

        expressionStatement.accept(trackingStatementVisitor);

        assertThat(trackingStatementVisitor.getExpressionStatements())
                .containsExactly(expressionStatement);
    }

}