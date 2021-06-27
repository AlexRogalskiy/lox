package com.caco3.lox.statement.visitor;

import com.caco3.lox.statement.BlockStatement;
import com.caco3.lox.statement.PrintStatement;
import com.caco3.lox.statement.VariableDeclarationStatement;

public interface StatementVisitor {
    default void visitPrintStatement(PrintStatement printStatement) {
    }

    default void visitVariableDeclarationStatement(VariableDeclarationStatement variableDeclarationStatement) {
    }

    default void visitBlockStatement(BlockStatement blockStatement) {
    }
}
