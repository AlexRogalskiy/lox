package com.caco3.lox;

import com.caco3.lox.interpreter.InterpreterVisitor;
import com.caco3.lox.lexer.DefaultLexer;
import com.caco3.lox.lexer.Lexer;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.parser.DefaultParser;
import com.caco3.lox.statement.Statement;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@AllArgsConstructor
public class LoxShell {
    private final PrintStream output;
    private final Scanner input;

    public void run() {
        List<String> strings = new ArrayList<>();
        while (input.hasNextLine()) {
            strings.add(input.nextLine());
        }

        Lexer lexer = new DefaultLexer(String.join("\n", strings));
        List<Token> tokens = lexer.parseTokens();
        List<Statement> statements = new DefaultParser(tokens).parseStatements();
        InterpreterVisitor interpreterVisitor = InterpreterVisitor.of(output);
        statements.forEach(it -> it.accept(interpreterVisitor));
    }

    public static void main(String[] args) {
        Scanner input;
        if (args.length == 1) {
            input = new Scanner(readScriptFromFile(args[0]));
        } else {
            input = new Scanner(System.in);
        }
        new LoxShell(System.out, input).run();
    }

    private static String readScriptFromFile(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
