package com.caco3.lox.expression.visitor;

import com.caco3.lox.expression.AssignmentExpression;
import com.caco3.lox.expression.BinaryExpression;
import com.caco3.lox.expression.CallExpression;
import com.caco3.lox.expression.GroupingExpression;
import com.caco3.lox.expression.IdentifierExpression;
import com.caco3.lox.expression.LiteralExpression;
import com.caco3.lox.expression.UnaryExpression;

public interface ExpressionVisitor {
    void visitBinaryExpression(BinaryExpression binaryExpression);

    void visitUnaryExpression(UnaryExpression unaryExpression);

    void visitLiteralExpression(LiteralExpression literalExpression);

    void visitGroupingExpression(GroupingExpression groupingExpression);

    void visitIdentifierExpression(IdentifierExpression identifierExpression);

    void visitAssignmentExpression(AssignmentExpression assignmentExpression);

    void visitCallExpression(CallExpression callExpression);
}
