package com.caco3.lox.statement;

import com.caco3.lox.lexer.Token;
import com.caco3.lox.statement.visitor.StatementVisitor;
import com.caco3.lox.util.Assert;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Tolerate;

import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
public class BlockStatement implements Statement {
    private final Token openingBracket;
    private final List<Statement> statements;
    private final Token closingBracket;

    private BlockStatement(Token openingBracket, List<Statement> statements, Token closingBracket) {
        Assert.notNull(openingBracket, "openingBracket == null");
        Assert.notNull(statements, "statements == null");
        Assert.notNull(closingBracket, "closingBracket == null");

        this.openingBracket = openingBracket;
        this.statements = statements;
        this.closingBracket = closingBracket;
    }

    public static BlockStatement of(Token openingBracket, List<Statement> statements, Token closingBracket) {
        return new BlockStatement(openingBracket, statements, closingBracket);
    }

    @Override
    public void accept(StatementVisitor statementVisitor) {
        Assert.notNull(statementVisitor, "statementVisitor == null");

        statementVisitor.visitBlockStatement(this);
    }
}
