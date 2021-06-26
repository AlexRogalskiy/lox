package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.TrackingExpressionVisitor;
import com.caco3.lox.lexer.Token;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnaryExpressionTest {
    @Test
    void acceptInvokesMethodForUnaryExpressions() {
        TrackingExpressionVisitor trackingExpressionVisitor = new TrackingExpressionVisitor();
        UnaryExpression expression = UnaryExpression.of(
                Token.of("-", 1, Token.Type.MINUS),
                LiteralExpression.of(Token.of("abc", 1, Token.Type.STRING_LITERAL))
        );

        expression.accept(trackingExpressionVisitor);

        assertThat(trackingExpressionVisitor.getVisitedUnaryExpressions())
                .containsExactly(expression);
    }
}