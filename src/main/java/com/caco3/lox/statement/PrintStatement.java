package com.caco3.lox.statement;

import com.caco3.lox.expression.Expression;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.statement.visitor.StatementVisitor;
import com.caco3.lox.util.Assert;
import lombok.Value;

@Value(staticConstructor = "of")
public class PrintStatement implements Statement {
    Token printToken;
    Expression expression;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        Assert.notNull(statementVisitor, "statementVisitor == null");

        statementVisitor.visitPrintStatement(this);
    }
}
