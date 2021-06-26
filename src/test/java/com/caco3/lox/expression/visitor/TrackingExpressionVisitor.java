package com.caco3.lox.expression.visitor;

import com.caco3.lox.expression.BinaryExpression;
import com.caco3.lox.expression.Expression;
import com.caco3.lox.expression.GroupingExpression;
import com.caco3.lox.expression.LiteralExpression;
import com.caco3.lox.expression.UnaryExpression;
import com.caco3.lox.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link TrackingExpressionVisitor} is an {@link ExpressionVisitor} that remembers all expression it has visited.
 * <p>
 * They are available through the following methods:
 * <ol>
 *     <li>{@link #getVisitedBinaryExpressions()}</li>
 *     <li>{@link #getVisitedGroupingExpressions()}</li>
 *     <li>{@link #getVisitedLiteralExpressions()}</li>
 *     <li>{@link #getVisitedUnaryExpressions()}</li>
 *     <li>{@link #getAllVisitedExpressions()}</li>
 * </ol>
 */
public class TrackingExpressionVisitor implements ExpressionVisitor {
    private final List<Expression> allVisitedExpressions = new ArrayList<>();
    private final List<Expression> visitedBinaryExpressions = new ArrayList<>();
    private final List<Expression> visitedUnaryExpressions = new ArrayList<>();
    private final List<Expression> visitedLiteralExpressions = new ArrayList<>();
    private final List<Expression> visitedGroupingExpressions = new ArrayList<>();

    @Override
    public void visitBinaryExpression(BinaryExpression binaryExpression) {
        Assert.notNull(binaryExpression, "binaryExpression == null");

        allVisitedExpressions.add(binaryExpression);
        visitedBinaryExpressions.add(binaryExpression);
    }

    @Override
    public void visitUnaryExpression(UnaryExpression unaryExpression) {
        Assert.notNull(unaryExpression, "unaryExpression == null");

        allVisitedExpressions.add(unaryExpression);
        visitedUnaryExpressions.add(unaryExpression);
    }

    @Override
    public void visitLiteralExpression(LiteralExpression literalExpression) {
        Assert.notNull(literalExpression, "literalExpression == null");

        allVisitedExpressions.add(literalExpression);
        visitedLiteralExpressions.add(literalExpression);
    }

    @Override
    public void visitGroupingExpression(GroupingExpression groupingExpression) {
        Assert.notNull(groupingExpression, "groupingExpression == null");

        allVisitedExpressions.add(groupingExpression);
        visitedGroupingExpressions.add(groupingExpression);
    }

    public List<Expression> getAllVisitedExpressions() {
        return Collections.unmodifiableList(allVisitedExpressions);
    }

    public List<Expression> getVisitedBinaryExpressions() {
        return Collections.unmodifiableList(visitedBinaryExpressions);
    }

    public List<Expression> getVisitedUnaryExpressions() {
        return Collections.unmodifiableList(visitedUnaryExpressions);
    }

    public List<Expression> getVisitedLiteralExpressions() {
        return Collections.unmodifiableList(visitedLiteralExpressions);
    }

    public List<Expression> getVisitedGroupingExpressions() {
        return visitedGroupingExpressions;
    }
}
