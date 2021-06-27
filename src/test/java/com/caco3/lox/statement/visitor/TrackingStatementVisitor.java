package com.caco3.lox.statement.visitor;

import com.caco3.lox.statement.BlockStatement;
import com.caco3.lox.statement.PrintStatement;
import com.caco3.lox.statement.Statement;
import com.caco3.lox.statement.VariableDeclarationStatement;
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
}
