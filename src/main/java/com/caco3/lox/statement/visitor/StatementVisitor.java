package com.caco3.lox.statement.visitor;

import com.caco3.lox.statement.BlockStatement;
import com.caco3.lox.statement.ExpressionStatement;
import com.caco3.lox.statement.ForStatement;
import com.caco3.lox.statement.IfStatement;
import com.caco3.lox.statement.PrintStatement;
import com.caco3.lox.statement.VariableDeclarationStatement;
import com.caco3.lox.statement.WhileStatement;

public interface StatementVisitor {
    void visitPrintStatement(PrintStatement printStatement);

    void visitVariableDeclarationStatement(VariableDeclarationStatement variableDeclarationStatement);

    void visitBlockStatement(BlockStatement blockStatement);

    void visitExpressionStatement(ExpressionStatement expressionStatement);

    void visitIfStatement(IfStatement ifStatement);

    void visitWhileStatement(WhileStatement whileStatement);

    void visitForStatement(ForStatement forStatement);
}
