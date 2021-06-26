package com.caco3.lox.statement.visitor;

import com.caco3.lox.statement.PrintStatement;

public interface StatementVisitor {
    default void visitPrintStatement(PrintStatement printStatement) {
    }
}
