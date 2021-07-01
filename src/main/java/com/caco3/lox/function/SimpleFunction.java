package com.caco3.lox.function;

import com.caco3.lox.environment.Scope;
import com.caco3.lox.interpreter.InterpreterVisitor;
import com.caco3.lox.lexer.Token;
import com.caco3.lox.statement.FunctionDeclarationStatement;
import com.caco3.lox.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.util.List;

@Slf4j
public class SimpleFunction implements Invocable {
    private final FunctionDeclarationStatement declaration;
    private final Scope scope;
    private final PrintStream printStream;

    public SimpleFunction(FunctionDeclarationStatement declaration, Scope scope, PrintStream printStream) {
        Assert.notNull(declaration, "declaration == null");
        Assert.notNull(scope, "scope == null");
        Assert.notNull(printStream, "printStream == null");

        this.declaration = declaration;
        this.scope = scope;
        this.printStream = printStream;
    }

    @Override
    public Object invoke(List<Object> arguments) {
        Assert.notNull(arguments, "arguments == null");
        logger.trace("Invoking function = {} with arguments = {}, current scope = {}",
                declaration.getName().getValue(), arguments, scope);

        Scope scope = this.scope.newChild();

        List<Token> parameters = declaration.getParameters();
        Assert.isTrue(parameters.size() == arguments.size(),
                () -> declaration + " accepts " + parameters.size()
                      + " arguments, but provided " + arguments.size()
                      + " arguments = " + arguments);

        for (int i = 0; i < parameters.size(); i++) {
            scope = scope.put(parameters.get(i).getValue(), arguments.get(i));
        }

        InterpreterVisitor interpreterVisitor = new InterpreterVisitor(printStream, scope);
        declaration.getBlock().accept(interpreterVisitor);
        return interpreterVisitor.getEvaluatedValue();
    }
}
