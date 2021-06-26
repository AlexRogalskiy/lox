package com.caco3.lox.interpreter;

import com.caco3.lox.expression.BinaryExpression;
import com.caco3.lox.expression.Expression;
import com.caco3.lox.expression.GroupingExpression;
import com.caco3.lox.expression.IdentifierExpression;
import com.caco3.lox.expression.LiteralExpression;
import com.caco3.lox.expression.UnaryExpression;
import com.caco3.lox.expression.visitor.ExpressionVisitor;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.statement.PrintStatement;
import com.caco3.lox.statement.VariableDeclarationStatement;
import com.caco3.lox.statement.visitor.StatementVisitor;
import com.caco3.lox.util.Assert;
import lombok.RequiredArgsConstructor;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class InterpreterVisitor implements StatementVisitor, ExpressionVisitor {
    private final PrintStream printStream;
    private final Map<String, Object> variables;
    private Object evaluatedValue;


    public InterpreterVisitor(PrintStream printStream) {
        this.printStream = printStream;
        this.variables = new LinkedHashMap<>();
    }

    public InterpreterVisitor(PrintStream printStream, Map<String, Object> variables) {
        this.printStream = printStream;
        this.variables = variables;
    }

    public static InterpreterVisitor of(PrintStream printStream) {
        return new InterpreterVisitor(printStream);
    }

    @Override
    public void visitBinaryExpression(BinaryExpression binaryExpression) {
        Assert.notNull(binaryExpression, "binaryExpression == null");
        Object leftValue = evaluate(binaryExpression.getLeft());
        Object rightValue = evaluate(binaryExpression.getRight());

        Token token = binaryExpression.getToken();

        evaluatedValue = evaluateBinary(leftValue, rightValue, token);
    }

    @Override
    public void visitUnaryExpression(UnaryExpression unaryExpression) {
        Assert.notNull(unaryExpression, "unaryExpression == null");

        Object target = evaluate(unaryExpression.getExpression());
        Token token = unaryExpression.getToken();
        evaluatedValue = evaluateUnary(target, token);
    }

    private Object evaluateUnary(Object target, Token token) {
        //noinspection ChainOfInstanceofChecks
        if (target instanceof Integer && token.getType() == Token.Type.MINUS) {
            return -(Integer) target;
        }
        if (target instanceof Boolean && token.getType() == Token.Type.BANG) {
            return !(Boolean) target;
        }
        throw new IllegalStateException("Unsupported " + token + " for target = " + target);
    }

    @Override
    public void visitLiteralExpression(LiteralExpression literalExpression) {
        Assert.notNull(literalExpression, "literalExpression == null");

        Token token = literalExpression.getToken();
        evaluatedValue = evaluateLiteral(token);
    }

    private Object evaluateLiteral(Token token) {
        Assert.isTrue(token.getType() == Token.Type.NUMBER_LITERAL
                      || token.getType() == Token.Type.STRING_LITERAL,
                () -> token + " must be either " + Token.Type.NUMBER_LITERAL + " or " + Token.Type.STRING_LITERAL);
        if (token.getType() == Token.Type.NUMBER_LITERAL) {
            return Integer.parseInt(token.getValue());
        } else {
            return token.getValue();
        }
    }

    @Override
    public void visitGroupingExpression(GroupingExpression groupingExpression) {
        Expression expression = groupingExpression;
        while (expression instanceof GroupingExpression) {
            expression = ((GroupingExpression) expression).getExpression();
        }
        evaluatedValue = evaluate(expression);
    }

    @Override
    public void visitPrintStatement(PrintStatement printStatement) {
        printStream.print(evaluate(printStatement.getExpression()));
    }

    @Override
    public void visitVariableDeclarationStatement(VariableDeclarationStatement variableDeclarationStatement) {
        String name = variableDeclarationStatement.getName().getValue();
        Object value = evaluate(variableDeclarationStatement.getInitializer());

        variables.put(name, value);
    }

    @Override
    public void visitIdentifierExpression(IdentifierExpression identifierExpression) {
        evaluatedValue = variables.get(identifierExpression.getName().getValue());
    }

    private Object evaluate(
            Expression expression
    ) {
        Assert.notNull(expression, "expression == null");

        InterpreterVisitor interpreterVisitor = new InterpreterVisitor(printStream, variables);
        expression.accept(interpreterVisitor);
        return interpreterVisitor.evaluatedValue;
    }

    private Object evaluateBinary(Object left, Object right, Token token) {
        //noinspection ChainOfInstanceofChecks
        if (left instanceof String && right instanceof String) {
            if (token.getType() == Token.Type.PLUS) {
                return String.valueOf(left) + right;
            }
            throw new IllegalStateException("Unsupported token = " + token + " for String operands");
        }
        if (left instanceof Integer && right instanceof Integer) {
            int a = (Integer) left;
            int b = (Integer) right;
            if (token.getType() == Token.Type.PLUS) {
                return a + b;
            } else if (token.getType() == Token.Type.MINUS) {
                return a - b;
            } else if (token.getType() == Token.Type.STAR) {
                return a * b;
            } else if (token.getType() == Token.Type.SLASH) {
                return a / b;
            } else if (token.getType() == Token.Type.GREATER) {
                return a > b;
            } else if (token.getType() == Token.Type.GREATER_EQUAL) {
                return a >= b;
            } else if (token.getType() == Token.Type.EQUAL_EQUAL) {
                return a == b;
            } else if (token.getType() == Token.Type.BANG_EQUAL) {
                return a != b;
            } else if (token.getType() == Token.Type.LESS) {
                return a < b;
            } else if (token.getType() == Token.Type.LESS_EQUAL) {
                return a <= b;
            }
            throw new IllegalStateException("Unsupported token for numbers " + token);
        }
        throw new IllegalStateException("Unsupported token = " + token
                                        + ", for operands = '" + left + "' and '" + right + "'");
    }
}
