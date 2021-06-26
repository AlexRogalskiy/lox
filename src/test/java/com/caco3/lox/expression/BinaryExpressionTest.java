package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.TrackingExpressionVisitor;
import com.caco3.lox.lexer.Token;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BinaryExpressionTest {
    @Test
    void acceptImplementedCorrectly() {
        TrackingExpressionVisitor trackingExpressionVisitor = new TrackingExpressionVisitor();

        BinaryExpression binaryExpression = BinaryExpression.of(
                LiteralExpression.of(Token.of("abc", 1, Token.Type.STRING_LITERAL)),
                LiteralExpression.of(Token.of("abc", 1, Token.Type.STRING_LITERAL)),
                Token.of("+", 1, Token.Type.PLUS)
        );

        binaryExpression.accept(trackingExpressionVisitor);

        assertThat(trackingExpressionVisitor.getVisitedBinaryExpressions())
                .containsExactly(binaryExpression);
    }
}