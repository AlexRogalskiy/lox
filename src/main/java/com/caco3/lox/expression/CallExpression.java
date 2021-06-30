package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.ExpressionVisitor;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.util.Assert;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class CallExpression implements Expression {
    Expression callee;
    Token leftParenthesis;
    List<Expression> arguments;
    Token rightParenthesis;

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        Assert.notNull(expressionVisitor, "expressionVisitor");

        expressionVisitor.visitCallExpression(this);
    }
}
