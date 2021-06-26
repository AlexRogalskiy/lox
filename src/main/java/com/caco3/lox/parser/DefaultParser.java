package com.caco3.lox.parser;

import com.caco3.lox.expression.BinaryExpression;
import com.caco3.lox.expression.Expression;
import com.caco3.lox.expression.GroupingExpression;
import com.caco3.lox.expression.IdentifierExpression;
import com.caco3.lox.expression.LiteralExpression;
import com.caco3.lox.expression.UnaryExpression;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.statement.PrintStatement;
import com.caco3.lox.statement.Statement;
import com.caco3.lox.statement.VariableDeclarationStatement;
import com.caco3.lox.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class DefaultParser implements Parser {
    private final List<Token> tokens;
    private int currentTokenIndex = 0;

    public DefaultParser(List<Token> tokens) {
        Assert.notNull(tokens, "tokens == null");

        this.tokens = tokens;
    }

    @Override
    public List<Statement> parseStatements() {
        List<Statement> statements = new ArrayList<>();
        while (hasTokens()) {
            statements.add(nextDeclaration());
        }
        return statements;
    }

    private Statement nextDeclaration() {
        if (currentTokenIs(Token.Type.VAR)) {
            advanceToken();
            Token identifier = advanceToken();
            Assert.state(identifier.getType() == Token.Type.IDENTIFIER,
                    () -> identifier + " must be " + Token.Type.IDENTIFIER);

            Expression initializer = null;
            if (currentTokenIs(Token.Type.EQUAL)) {
                advanceToken();
                initializer = nextExpression();
            }
            consumeExactly(Token.Type.SEMICOLON);
            return VariableDeclarationStatement.of(identifier, initializer);
        }
        return nextStatement();
    }

    private Statement nextStatement() {
        if (currentTokenIs(Token.Type.PRINT)) {
            PrintStatement printStatement = PrintStatement.of(advanceToken(), nextExpression());
            consumeExactly(Token.Type.SEMICOLON);
            return printStatement;
        }
        throw new IllegalStateException("Not implemented");
    }

    private void consumeExactly(Token.Type type) {
        if (!currentTokenIs(type)) {
            throw new IllegalStateException("Unexpected token " + type);
        }
        advanceToken();
    }

    private Expression nextExpression() {
        return nextEquality();
    }

    private Expression nextEquality() {
        Expression expression = nextComparison();
        while (currentTokenIs(Token.Type.EQUAL_EQUAL)
               || currentTokenIs(Token.Type.BANG_EQUAL)) {
            Token token = advanceToken();
            expression = BinaryExpression.of(expression, nextComparison(), token);
        }
        return expression;
    }

    private Expression nextComparison() {
        Expression expression = nextTerm();
        while (currentTokenIs(Token.Type.GREATER)
               || currentTokenIs(Token.Type.GREATER_EQUAL)
               || currentTokenIs(Token.Type.LESS)
               || currentTokenIs(Token.Type.LESS_EQUAL)) {
            Token token = advanceToken();
            expression = BinaryExpression.of(expression, nextTerm(), token);
        }
        return expression;
    }

    private Expression nextTerm() {
        Expression expression = nextFactor();
        while (currentTokenIs(Token.Type.PLUS) || currentTokenIs(Token.Type.MINUS)) {
            Token token = advanceToken();
            expression = BinaryExpression.of(expression, nextFactor(), token);
        }
        return expression;
    }

    private Expression nextFactor() {
        Expression expression = nextUnary();
        while (currentTokenIs(Token.Type.STAR) || currentTokenIs(Token.Type.SLASH)) {
            Token token = advanceToken();
            expression = BinaryExpression.of(expression, nextUnary(), token);
        }
        return expression;
    }

    private Expression nextUnary() {
        if (currentTokenIs(Token.Type.BANG) || currentTokenIs(Token.Type.MINUS)) {
            Token token = advanceToken();
            return UnaryExpression.of(token, nextUnary());
        }
        return nextPrimary();
    }

    private Expression nextPrimary() {
        if (currentTokenIs(Token.Type.STRING_LITERAL) || currentTokenIs(Token.Type.NUMBER_LITERAL)) {
            return LiteralExpression.of(advanceToken());
        }
        if (currentTokenIs(Token.Type.LEFT_PARENTHESIS)) {
            advanceToken();
            Expression expression = nextExpression();
            consumeExactly(Token.Type.RIGHT_PARENTHESIS);
            return GroupingExpression.of(expression);
        }
        if (currentTokenIs(Token.Type.IDENTIFIER)) {
            return IdentifierExpression.of(advanceToken());
        }
        throw new IllegalStateException("Unsupported token = " + advanceToken());
    }

    private boolean hasTokens() {
        return currentTokenIndex < tokens.size();
    }

    private boolean currentTokenIs(Token.Type type) {
        Assert.notNull(type, "type == null");
        if (currentTokenIndex >= tokens.size()) {
            return false;
        }

        return tokens.get(currentTokenIndex).getType() == type;
    }

    private Token advanceToken() {
        return tokens.get(currentTokenIndex++);
    }
}
