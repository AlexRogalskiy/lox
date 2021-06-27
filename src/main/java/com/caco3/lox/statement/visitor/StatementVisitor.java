package com.caco3.lox.statement.visitor;

import com.caco3.lox.statement.BlockStatement;
import com.caco3.lox.statement.ExpressionStatement;
import com.caco3.lox.statement.ForStatement;
import com.caco3.lox.statement.IfStatement;
import com.caco3.lox.statement.PrintStatement;
import com.caco3.lox.statement.VariableDeclarationStatement;
import com.caco3.lox.statement.WhileStatement;

public interface StatementVisitor {
    default void visitPrintStatement(PrintStatement printStatement) {
    }

    default void visitVariableDeclarationStatement(VariableDeclarationStatement variableDeclarationStatement) {
    }

    default void visitBlockStatement(BlockStatement blockStatement) {
    }

    default void visitExpressionStatement(ExpressionStatement expressionStatement) {
    }

    default void visitIfStatement(IfStatement ifStatement) {
    }

    default void visitWhileStatement(WhileStatement whileStatement) {
    }

    default void visitForStatement(ForStatement forStatement) {
    }
}
