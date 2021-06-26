package com.caco3.lox.lexer;

import java.util.List;

public interface Lexer {
    List<Token> parseTokens();
}
