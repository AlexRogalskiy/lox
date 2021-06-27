package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.TrackingExpressionVisitor;
import com.caco3.lox.lexer.Token;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AssignmentExpressionTest {
    @Test
    void visitorImplementedCorrectly() {
        AssignmentExpression assignmentExpression = AssignmentExpression.of(
                Token.of("x", 1, Token.Type.IDENTIFIER),
                Token.of("=", 1, Token.Type.EQUAL),
                LiteralExpression.of(Token.of("1", 1, Token.Type.NUMBER_LITERAL))
        );

        TrackingExpressionVisitor visitor = new TrackingExpressionVisitor();
        assignmentExpression.accept(visitor);

        assertThat(visitor.getVisitedAssignmentExpressions())
                .containsExactly(assignmentExpression);
    }

}