package com.caco3.lox.statement;

import com.caco3.lox.statement.visitor.StatementVisitor;

public sealed interface Statement permits
        BlockStatement,
        ExpressionStatement,
        ForStatement,
        FunctionDeclarationStatement,
        IfStatement,
        PrintStatement,
        ReturnStatement,
        VariableDeclarationStatement,
        WhileStatement {
    void accept(StatementVisitor statementVisitor);
}
