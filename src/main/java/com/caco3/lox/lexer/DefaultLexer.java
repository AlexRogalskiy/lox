package com.caco3.lox.lexer;

import com.caco3.lox.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class DefaultLexer implements Lexer {
    private static final Map<String, Token.Type> KEYWORDS = Map.ofEntries(
            entry("if", Token.Type.IF),
            entry("else", Token.Type.ELSE),
            entry("var", Token.Type.VAR)
    );
    private static final Map<Character, Token.Type> SINGLE_CHARACTER_TOKENS = Map.ofEntries(
            entry('(', Token.Type.LEFT_PARENTHESIS),
            entry(')', Token.Type.RIGHT_PARENTHESIS),
            entry('+', Token.Type.PLUS),
            entry('-', Token.Type.MINUS),
            entry('*', Token.Type.STAR),
            entry('/', Token.Type.SLASH),
            entry('{', Token.Type.LEFT_BRACKET),
            entry('}', Token.Type.RIGHT_BRACKET),
            entry('>', Token.Type.GREATER),
            entry('<', Token.Type.LESS),
            entry('=', Token.Type.EQUAL)
    );


    private final String text;
    private int currentIndex = 0;
    private int line = 1;

    public DefaultLexer(String text) {
        Assert.notNull(text, () -> "text == null");

        this.text = text;
    }

    @Override
    public List<Token> parseTokens() {
        List<Token> tokens = new ArrayList<>();

        while (currentIndex < text.length()) {
            skipWhitespaces();
            if (peek() == '\n') {
                line++;
                currentIndex++;
                continue;
            }
            if (hasNextTwoCharacterToken()) {
                tokens.add(nextTwoCharactersToken());
                continue;
            }

            Token.Type type = SINGLE_CHARACTER_TOKENS.get(peek());
            if (type != null) {
                tokens.add(Token.of(peek() + "", line, type));
                currentIndex++;
                continue;
            }

            if (Character.isAlphabetic(peek())) {
                tokens.add(nextIdentifier());
            } else if (Character.isDigit(peek())) {
                tokens.add(nextNumberLiteral());
            } else if (peek() == '"') {
                tokens.add(nextStringLiteral());
            } else {
                throw new IllegalStateException("Unknown token = '" + peek() + "'");
            }
            currentIndex++;
        }

        return tokens;
    }

    private Token nextTwoCharactersToken() {
        char first = peek();
        int second = peekAhead();

        currentIndex++;
        currentIndex++;
        if (first == '<' && second == '=') {
            return Token.of("<=", line, Token.Type.LESS_EQUAL);
        } else if (first == '=' && second == '=') {
            return Token.of("==", line, Token.Type.EQUAL_EQUAL);
        } else if (first == '!' && second == '=') {
            return Token.of("!=", line, Token.Type.BANG_EQUAL);
        } else if (first == '>' && second == '=') {
            return Token.of(">=", line, Token.Type.GREATER_EQUAL);
        }
        throw new IllegalStateException("Unreachable");
    }

    private Token nextStringLiteral() {
        currentIndex++;
        StringBuilder stringBuilder = new StringBuilder();
        int newLines = 0;
        while (peek() != '"') {
            if (peek() == '\n') {
                newLines++;
            }
            stringBuilder.append(peek());
            currentIndex++;
        }
        Token token = Token.of(stringBuilder.toString(), line, Token.Type.STRING_LITERAL);
        line += newLines;
        return token;
    }

    private int peekAhead() {
        if (currentIndex + 1 < text.length()) {
            return text.charAt(currentIndex + 1);
        }
        return -1;
    }

    private void skipWhitespaces() {
        while (peek() == ' ') {
            currentIndex++;
        }
    }

    private char peek() {
        return text.charAt(currentIndex);
    }

    private Token nextIdentifier() {
        StringBuilder identifier = new StringBuilder();
        while (currentIndex < text.length() && Character.isAlphabetic(text.charAt(currentIndex))) {
            identifier.append(text.charAt(currentIndex));
            currentIndex++;
        }

        String identifierAsString = identifier.toString();
        Token.Type tokenType = KEYWORDS.getOrDefault(identifierAsString, Token.Type.IDENTIFIER);
        Token token = Token.of(identifierAsString, line, tokenType);
        currentIndex--;
        return token;
    }

    private Token nextNumberLiteral() {
        StringBuilder number = new StringBuilder();
        while (currentIndex < text.length() && Character.isDigit(text.charAt(currentIndex))) {
            number.append(text.charAt(currentIndex));
            currentIndex++;
        }
        currentIndex--;
        return Token.of(number.toString(), line, Token.Type.NUMBER_LITERAL);
    }

    private boolean hasNextTwoCharacterToken() {
        if (peek() == '=' && peekAhead() == '=') {
            return true;
        }
        if (peek() == '!' && peekAhead() == '=') {
            return true;
        }
        if (peek() == '<' && peekAhead() == '=') {
            return true;
        }
        //noinspection RedundantIfStatement
        if (peek() == '>' && peekAhead() == '=') {
            return true;
        }
        return false;
    }
}
