package com.caco3.lox.lexer;

import lombok.Value;

@Value(staticConstructor = "of")
public class Token {
    enum Type {
        LEFT_PARENTHESIS,
        RIGHT_PARENTHESIS,
        NUMBER_LITERAL,
        MINUS,
        PLUS,
        STAR,
        SLASH,
        EQUAL,
        IF,
        ELSE,
        LEFT_BRACKET,
        RIGHT_BRACKET,
        LESS_EQUAL,
        LESS,
        EQUAL_EQUAL,
        BANG_EQUAL,
        GREATER_EQUAL,
        GREATER,
        IDENTIFIER,
        VAR,
        STRING_LITERAL
    }


    String value;
    int lineNumber;
    Type type;
}
