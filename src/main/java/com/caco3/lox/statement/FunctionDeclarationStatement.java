package com.caco3.lox.statement;

import com.caco3.lox.lexer.Token;
import com.caco3.lox.statement.visitor.StatementVisitor;
import com.caco3.lox.util.Assert;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class FunctionDeclarationStatement implements Statement {
    Token functionToken;
    Token name;
    List<Token> parameters;
    Statement block;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        Assert.notNull(statementVisitor, "statementVisitor == null");

        statementVisitor.visitFunctionDeclarationStatement(this);
    }
}
