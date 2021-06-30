package com.caco3.lox.expression.visitor;

import com.caco3.lox.expression.AssignmentExpression;
import com.caco3.lox.expression.BinaryExpression;
import com.caco3.lox.expression.CallExpression;
import com.caco3.lox.expression.Expression;
import com.caco3.lox.expression.GroupingExpression;
import com.caco3.lox.expression.IdentifierExpression;
import com.caco3.lox.expression.LiteralExpression;
import com.caco3.lox.expression.UnaryExpression;
import com.caco3.lox.util.Assert;
import lombok.Getter;

import java.util.ArrayList;
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
 *     <li>{@link #getVisitedAssignmentExpressions()}</li>
 *     <li>{@link #getVisitedIdentifierExpressions()}</li>
 * </ol>
 */
@Getter
public class TrackingExpressionVisitor implements ExpressionVisitor {
    private final List<Expression> allVisitedExpressions = new ArrayList<>();
    private final List<Expression> visitedBinaryExpressions = new ArrayList<>();
    private final List<Expression> visitedUnaryExpressions = new ArrayList<>();
    private final List<Expression> visitedLiteralExpressions = new ArrayList<>();
    private final List<Expression> visitedGroupingExpressions = new ArrayList<>();
    private final List<AssignmentExpression> visitedAssignmentExpressions = new ArrayList<>();
    private final List<IdentifierExpression> visitedIdentifierExpressions = new ArrayList<>();
    private final List<CallExpression> visitedCallExpressions = new ArrayList<>();

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

    @Override
    public void visitAssignmentExpression(AssignmentExpression assignmentExpression) {
        Assert.notNull(assignmentExpression, "assignmentExpression == null");

        visitedAssignmentExpressions.add(assignmentExpression);
        allVisitedExpressions.add(assignmentExpression);
    }

    @Override
    public void visitIdentifierExpression(IdentifierExpression identifierExpression) {
        Assert.notNull(identifierExpression, "identifierExpression == null");

        visitedIdentifierExpressions.add(identifierExpression);
        allVisitedExpressions.add(identifierExpression);
    }

    @Override
    public void visitCallExpression(CallExpression callExpression) {
        Assert.notNull(callExpression, "callExpression == null");

        visitedCallExpressions.add(callExpression);
        allVisitedExpressions.add(callExpression);
    }
}
