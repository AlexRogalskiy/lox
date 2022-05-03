package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.ExpressionVisitor;

public sealed interface Expression permits
        AssignmentExpression,
        BinaryExpression,
        CallExpression,
        GroupingExpression,
        IdentifierExpression,
        LiteralExpression,
        UnaryExpression {
    void accept(ExpressionVisitor expressionVisitor);
}
