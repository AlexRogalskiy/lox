package com.caco3.lox.expression.visitor;

import com.caco3.lox.expression.BinaryExpression;
import com.caco3.lox.expression.GroupingExpression;
import com.caco3.lox.expression.LiteralExpression;
import com.caco3.lox.expression.UnaryExpression;

public interface ExpressionVisitor {
    default void visitBinaryExpression(BinaryExpression binaryExpression) {
    }

    default void visitUnaryExpression(UnaryExpression unaryExpression) {
    }

    default void visitLiteralExpression(LiteralExpression literalExpression) {
    }

    default void visitGroupingExpression(GroupingExpression groupingExpression) {
    }
}
