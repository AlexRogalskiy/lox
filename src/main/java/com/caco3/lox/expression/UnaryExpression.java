package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.ExpressionVisitor;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.util.Assert;
import lombok.Value;

import java.util.Set;

@Value(staticConstructor = "of")
public class UnaryExpression implements Expression {
    private static final Set<Token.Type> UNARY_TOKEN_TYPES = Set.of(
            Token.Type.MINUS
    );

    Token token;
    Expression expression;

    private UnaryExpression(Token token, Expression expression) {
        Assert.notNull(token, "token == null");
        Assert.notNull(expression, "expression == null");
        Assert.isTrue(UNARY_TOKEN_TYPES.contains(token.getType()),
                () -> token + " must have one of the following types: " + UNARY_TOKEN_TYPES);

        this.token = token;
        this.expression = expression;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        Assert.notNull(expressionVisitor, "expressionVisitor = null");
        expressionVisitor.visitUnaryExpression(this);
    }
}
