package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.ExpressionVisitor;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.util.Assert;
import lombok.Value;

import java.util.Set;

@Value(staticConstructor = "of")
public class LiteralExpression implements Expression {
    private static final Set<Token.Type> LITERAL_TOKEN_TYPES =
            Set.of(
                    Token.Type.NUMBER_LITERAL,
                    Token.Type.STRING_LITERAL
            );

    Token token;

    private LiteralExpression(Token token) {
        Assert.notNull(token, "token == null");
        Assert.isTrue(LITERAL_TOKEN_TYPES.contains(token.getType()),
                () -> token + " must have one of the following types: '" + LITERAL_TOKEN_TYPES);

        this.token = token;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        Assert.notNull(expressionVisitor, "expressionVisitor == null");
        expressionVisitor.visitLiteralExpression(this);
    }
}
