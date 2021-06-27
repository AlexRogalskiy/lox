package com.caco3.lox.statement.visitor;

import com.caco3.lox.statement.BlockStatement;
import com.caco3.lox.statement.ExpressionStatement;
import com.caco3.lox.statement.ForStatement;
import com.caco3.lox.statement.IfStatement;
import com.caco3.lox.statement.PrintStatement;
import com.caco3.lox.statement.Statement;
import com.caco3.lox.statement.VariableDeclarationStatement;
import com.caco3.lox.statement.WhileStatement;
import com.caco3.lox.util.Assert;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TrackingStatementVisitor implements StatementVisitor {
    private final List<Statement> allStatements = new ArrayList<>();
    private final List<PrintStatement> printStatements = new ArrayList<>();
    private final List<VariableDeclarationStatement> variableDeclarationStatements = new ArrayList<>();
    private final List<BlockStatement> blockStatements = new ArrayList<>();
    private final List<ExpressionStatement> expressionStatements = new ArrayList<>();
    private final List<IfStatement> ifStatements = new ArrayList<>();
    private final List<WhileStatement> whileStatements = new ArrayList<>();
    private final List<ForStatement> forStatements = new ArrayList<>();

    @Override
    public void visitPrintStatement(PrintStatement printStatement) {
        Assert.notNull(printStatement, "printStatement == null");

        printStatements.add(printStatement);
        allStatements.add(printStatement);
    }

    @Override
    public void visitVariableDeclarationStatement(VariableDeclarationStatement variableDeclarationStatement) {
        Assert.notNull(variableDeclarationStatement, "variableDeclarationStatement == null");

        variableDeclarationStatements.add(variableDeclarationStatement);
        allStatements.add(variableDeclarationStatement);
    }

    @Override
    public void visitBlockStatement(BlockStatement blockStatement) {
        Assert.notNull(blockStatement, "blockStatement == null");

        blockStatements.add(blockStatement);
        allStatements.add(blockStatement);
    }

    @Override
    public void visitExpressionStatement(ExpressionStatement expressionStatement) {
        Assert.notNull(expressionStatement, "expressionStatement == null");

        expressionStatements.add(expressionStatement);
        allStatements.add(expressionStatement);
    }

    @Override
    public void visitIfStatement(IfStatement ifStatement) {
        Assert.notNull(ifStatement, "ifStatement == null");

        ifStatements.add(ifStatement);
        allStatements.add(ifStatement);
    }

    @Override
    public void visitWhileStatement(WhileStatement whileStatement) {
        Assert.notNull(whileStatement, "whileStatement == null");

        whileStatements.add(whileStatement);
        allStatements.add(whileStatement);
    }

    @Override
    public void visitForStatement(ForStatement forStatement) {
        Assert.notNull(forStatement, "forStatement == null");

        forStatements.add(forStatement);
        allStatements.add(forStatement);
    }
}
