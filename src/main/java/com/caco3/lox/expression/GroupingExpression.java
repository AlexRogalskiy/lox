package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.ExpressionVisitor;
import com.caco3.lox.util.Assert;
import lombok.Value;

@Value(staticConstructor = "of")
public class GroupingExpression implements Expression {
    Expression expression;

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        Assert.notNull(expressionVisitor, "expressionVisitor == null");
        expressionVisitor.visitGroupingExpression(this);
    }
}
