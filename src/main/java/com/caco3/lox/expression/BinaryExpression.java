package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.ExpressionVisitor;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.util.Assert;
import lombok.Value;

@Value(staticConstructor = "of")
public class BinaryExpression implements Expression {
    Expression left;
    Expression right;
    Token token;

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        Assert.notNull(expressionVisitor, "expressionVisitor == null");
        expressionVisitor.visitBinaryExpression(this);
    }
}
