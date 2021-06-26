package com.caco3.lox.expression.visitor;

import com.caco3.lox.expression.BinaryExpression;
import com.caco3.lox.expression.Expression;
import com.caco3.lox.expression.GroupingExpression;
import com.caco3.lox.expression.LiteralExpression;
import com.caco3.lox.expression.UnaryExpression;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.util.Assert;

public class InterpreterExpressionVisitor implements ExpressionVisitor {
    private Object value;

    private static Object interpretRecursively(Expression expression) {
        Assert.notNull(expression, "expression == null");
        InterpreterExpressionVisitor visitor = new InterpreterExpressionVisitor();
        expression.accept(visitor);
        return visitor.getValue();
    }

    @Override
    public void visitBinaryExpression(BinaryExpression binaryExpression) {
        Token token = binaryExpression.getToken();
        Object leftValue = interpretRecursively(binaryExpression.getLeft());
        Object rightValue = interpretRecursively(binaryExpression.getRight());
        if (token.getType() == Token.Type.PLUS) {
            if (leftValue instanceof String && rightValue instanceof String) {
                this.value = String.valueOf(leftValue) + rightValue;
            } else if (leftValue instanceof Integer && rightValue instanceof Integer) {
                this.value = ((Integer) leftValue) + ((Integer) rightValue);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public void visitUnaryExpression(UnaryExpression unaryExpression) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void visitLiteralExpression(LiteralExpression literalExpression) {
        Token token = literalExpression.getToken();
        Token.Type type = token.getType();
        Assert.isTrue(type == Token.Type.NUMBER_LITERAL || type == Token.Type.STRING_LITERAL,
                () -> "type of token must be either " + Token.Type.NUMBER_LITERAL + " or " + Token.Type.STRING_LITERAL
                      + ", but token was = " + token);

        if (type == Token.Type.STRING_LITERAL) {
            value = token.getValue();
        } else {
            value = Integer.parseInt(token.getValue());
        }
    }

    @Override
    public void visitGroupingExpression(GroupingExpression groupingExpression) {
        Expression expression = groupingExpression.getExpression();
        InterpreterExpressionVisitor visitor = new InterpreterExpressionVisitor();
        expression.accept(visitor);

        value = visitor.value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "InterpreterExpressionVisitor{" +
               "value=" + value +
               '}';
    }
}
