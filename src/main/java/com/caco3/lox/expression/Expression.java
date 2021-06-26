package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.ExpressionVisitor;

public interface Expression {
    void accept(ExpressionVisitor expressionVisitor);
}
