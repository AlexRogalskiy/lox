package com.caco3.lox.expression;

import com.caco3.lox.expression.visitor.ExpressionVisitor;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.util.Assert;
import lombok.Value;

@Value(staticConstructor = "of")
public class AssignmentExpression implements Expression {
    Token identifier;
    Token equalSign;
    Expression target;

    private AssignmentExpression(Token identifier, Token equalSign, Expression target) {
        Assert.notNull(identifier, "identifier == null");
        Assert.notNull(equalSign, "equalSign == null");
        Assert.notNull(target, "target == null");
        Assert.isTrue(equalSign.getType() == Token.Type.EQUAL,
                () -> equalSign + " must have '" + Token.Type.EQUAL + "' type");
        Assert.isTrue(identifier.getType() == Token.Type.IDENTIFIER,
                () -> identifier + " must have '" + Token.Type.IDENTIFIER + "' type");

        this.identifier = identifier;
        this.equalSign = equalSign;
        this.target = target;
    }

    @Override
    public void accept(ExpressionVisitor statementVisitor) {
        Assert.notNull(statementVisitor, "statementVisitor == null");

        statementVisitor.visitAssignmentExpression(this);
    }
}
