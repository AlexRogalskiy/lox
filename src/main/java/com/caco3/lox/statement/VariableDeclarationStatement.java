package com.caco3.lox.statement;

import com.caco3.lox.expression.Expression;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.statement.visitor.StatementVisitor;
import lombok.Value;

@Value(staticConstructor = "of")
public class VariableDeclarationStatement implements Statement {
    Token name;
    Expression initializer;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visitVariableDeclarationStatement(this);
    }
}
