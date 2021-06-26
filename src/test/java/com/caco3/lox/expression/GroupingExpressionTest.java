package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.TrackingExpressionVisitor;
import com.caco3.lox.lexer.Token;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GroupingExpressionTest {
    @Test
    void acceptInvokesMethodForGroupingExpressions() {
        TrackingExpressionVisitor visitor = new TrackingExpressionVisitor();
        GroupingExpression groupingExpression = GroupingExpression.of(
                LiteralExpression.of(
                        Token.of("abc", 1, Token.Type.STRING_LITERAL)));

        groupingExpression.accept(visitor);

        assertThat(visitor.getVisitedGroupingExpressions())
                .containsExactly(groupingExpression);
    }
}