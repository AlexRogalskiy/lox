package com.caco3.lox.parser;

import com.caco3.lox.statement.Statement;

import java.util.List;

public interface Parser {
    List<Statement> parseStatements();
}
