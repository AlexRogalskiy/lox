package com.caco3.lox.statement;

import com.caco3.lox.statement.visitor.StatementVisitor;

public interface Statement {
    void accept(StatementVisitor statementVisitor);
}
