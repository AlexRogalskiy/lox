package com.caco3.lox.parser;

import com.caco3.lox.expression.AssignmentExpression;
import com.caco3.lox.expression.BinaryExpression;
import com.caco3.lox.expression.CallExpression;
import com.caco3.lox.expression.Expression;
import com.caco3.lox.expression.GroupingExpression;
import com.caco3.lox.expression.IdentifierExpression;
import com.caco3.lox.expression.LiteralExpression;
import com.caco3.lox.expression.UnaryExpression;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.statement.BlockStatement;
import com.caco3.lox.statement.ExpressionStatement;
import com.caco3.lox.statement.ForStatement;
import com.caco3.lox.statement.FunctionDeclarationStatement;
import com.caco3.lox.statement.IfStatement;
import com.caco3.lox.statement.PrintStatement;
import com.caco3.lox.statement.ReturnStatement;
import com.caco3.lox.statement.Statement;
import com.caco3.lox.statement.VariableDeclarationStatement;
import com.caco3.lox.statement.WhileStatement;
import com.caco3.lox.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class DefaultParser implements Parser {
    private static final int MAX_ARGUMENTS_FOR_CALL = 255;

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
            return nextVariableDeclaration();
        }
        if (currentTokenIs(Token.Type.FUNCTION)) {
            return nextFunctionDeclaration();
        }
        return nextStatement();
    }

    private VariableDeclarationStatement nextVariableDeclaration() {
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

    private Statement nextFunctionDeclaration() {
        Token functionToken = consumeExactly(Token.Type.FUNCTION);
        Token functionName = advanceToken();
        consumeExactly(Token.Type.LEFT_PARENTHESIS);
        List<Token> parameters = new ArrayList<>();
        boolean first = true;
        while (!currentTokenIs(Token.Type.RIGHT_PARENTHESIS)) {
            if (!first) {
                consumeExactly(Token.Type.COMMA);
            }
            parameters.add(consumeExactly(Token.Type.IDENTIFIER));
            first = false;
        }
        consumeExactly(Token.Type.RIGHT_PARENTHESIS);
        BlockStatement block = nextBlock();
        return FunctionDeclarationStatement.of(functionToken, functionName, parameters, block);
    }

    private Statement nextStatement() {
        if (currentTokenIs(Token.Type.IF)) {
            return nextIfStatement();
        }
        if (currentTokenIs(Token.Type.WHILE)) {
            return nextWhileStatement();
        }
        if (currentTokenIs(Token.Type.FOR)) {
            return nextForStatement();
        }
        if (currentTokenIs(Token.Type.PRINT)) {
            PrintStatement printStatement = PrintStatement.of(advanceToken(), nextExpression());
            consumeExactly(Token.Type.SEMICOLON);
            return printStatement;
        }
        if (currentTokenIs(Token.Type.LEFT_BRACKET)) {
            return nextBlock();
        }
        if (currentTokenIs(Token.Type.RETURN)) {
            return nextReturnStatement();
        }
        return ExpressionStatement.of(nextExpression(), consumeExactly(Token.Type.SEMICOLON));
    }

    private Statement nextReturnStatement() {
        Token returnToken = consumeExactly(Token.Type.RETURN);
        Expression expression = null;
        if (!currentTokenIs(Token.Type.SEMICOLON)) {
            expression = nextExpression();
        }
        Token semicolon = consumeExactly(Token.Type.SEMICOLON);
        return ReturnStatement.of(returnToken, expression, semicolon);
    }

    private BlockStatement nextBlock() {
        Token openingBracket = consumeExactly(Token.Type.LEFT_BRACKET);
        List<Statement> statements = new ArrayList<>();
        while (!currentTokenIs(Token.Type.RIGHT_BRACKET)) {
            statements.add(nextDeclaration());
        }
        Token closingBracket = consumeExactly(Token.Type.RIGHT_BRACKET);
        return BlockStatement.of(openingBracket, statements, closingBracket);
    }

    private Statement nextForStatement() {
        Token forToken = advanceToken();
        consumeExactly(Token.Type.LEFT_PARENTHESIS);
        Statement initializer;
        if (currentTokenIs(Token.Type.VAR)) {
            initializer = nextDeclaration();
        } else {
            initializer = ExpressionStatement.of(nextExpression(), advanceToken());
        }
        Expression condition = nextExpression();
        consumeExactly(Token.Type.SEMICOLON);
        Expression action = nextExpression();
        consumeExactly(Token.Type.RIGHT_PARENTHESIS);
        Statement body = nextStatement();

        return ForStatement.of(forToken, initializer, condition, action, body);
    }

    private Statement nextWhileStatement() {
        Token whileToken = advanceToken();
        consumeExactly(Token.Type.LEFT_PARENTHESIS);
        Expression terminationCondition = nextExpression();
        consumeExactly(Token.Type.RIGHT_PARENTHESIS);
        Statement statement = nextStatement();
        return WhileStatement.of(whileToken, terminationCondition, statement);
    }

    private IfStatement nextIfStatement() {
        Token ifToken = advanceToken();
        consumeExactly(Token.Type.LEFT_PARENTHESIS);
        Expression condition = nextExpression();
        consumeExactly(Token.Type.RIGHT_PARENTHESIS);
        Statement thenBranch = nextStatement();
        Statement elseBranch = null;
        if (currentTokenIs(Token.Type.ELSE)) {
            advanceToken();
            elseBranch = nextStatement();
        }
        return IfStatement.of(ifToken, condition, thenBranch, elseBranch);
    }

    private Token consumeExactly(Token.Type type) {
        if (!currentTokenIs(type)) {
            throw new IllegalStateException("Expected " + type + " but got " + advanceToken());
        }
        return advanceToken();
    }

    private Expression nextExpression() {
        return nextAssignment();
    }

    private Expression nextAssignment() {
        Expression expression = nextEquality();
        if (currentTokenIs(Token.Type.EQUAL)) {
            Token equalSign = advanceToken();
            Expression target = nextAssignment();

            if (expression instanceof IdentifierExpression) {
                IdentifierExpression identifierExpression = (IdentifierExpression) expression;
                Token name = identifierExpression.getName();
                return AssignmentExpression.of(name, equalSign, target);
            }
        }
        return expression;
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
        return nextCall();
    }

    private Expression nextCall() {
        Expression maybeCall = nextPrimary();

        while (currentTokenIs(Token.Type.LEFT_PARENTHESIS)) {
            Token leftParenthesis = advanceToken();
            List<Expression> arguments = new ArrayList<>();
            while (!currentTokenIs(Token.Type.RIGHT_PARENTHESIS)) {
                arguments.add(nextExpression());
            }
            if (arguments.size() >= MAX_ARGUMENTS_FOR_CALL) {
                throw new IllegalStateException("Too many arguments for call " + maybeCall);
            }
            Token rightParenthesis = consumeExactly(Token.Type.RIGHT_PARENTHESIS);
            maybeCall = CallExpression.of(maybeCall, leftParenthesis, arguments, rightParenthesis);
        }
        return maybeCall;
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
