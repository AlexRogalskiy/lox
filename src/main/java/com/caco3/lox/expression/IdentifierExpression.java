package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.ExpressionVisitor;
import com.caco3.lox.lexer.Token;
import lombok.Value;

@Value(staticConstructor = "of")
public class IdentifierExpression implements Expression {
    Token name;

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visitIdentifierExpression(this);
    }
}
