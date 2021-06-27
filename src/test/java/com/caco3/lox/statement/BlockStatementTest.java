package com.caco3.lox.statement;

import com.caco3.lox.lexer.Token;
import com.caco3.lox.statement.visitor.TrackingStatementVisitor;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class BlockStatementTest {
    @Test
    void visitImplementedCorrectly() {
        BlockStatement statement = BlockStatement.of(
                Token.of("{", 1, Token.Type.LEFT_BRACKET),
                Collections.emptyList(),
                Token.of("}", 1, Token.Type.RIGHT_BRACKET)
        );

        TrackingStatementVisitor visitor = new TrackingStatementVisitor();
        statement.accept(visitor);

        assertThat(visitor.getBlockStatements())
                .containsExactly(statement);
    }
}