package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.TrackingExpressionVisitor;
import com.caco3.lox.lexer.Token;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LiteralExpressionTest {
    @Test
    void acceptInvokesMethodForLiteralExpressions() {
        TrackingExpressionVisitor visitor = new TrackingExpressionVisitor();
        LiteralExpression literalExpression = LiteralExpression.of(Token.of("abc", 1, Token.Type.STRING_LITERAL));

        literalExpression.accept(visitor);

        assertThat(visitor.getVisitedLiteralExpressions())
                .containsExactly(literalExpression);
    }
}