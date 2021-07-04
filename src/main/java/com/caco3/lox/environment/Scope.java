package com.caco3.lox.environment;

import com.caco3.lox.util.CanNotIgnoreReturnValue;
import com.caco3.lox.util.Nullable;

public interface Scope {
    static Scope create() {
        return SimpleScope.create();
    }

    /**
     * Get a value that a variable named {@code name} holds in the innermost scope.
     * <p>
     * If the current scope contains {@code name} then the value in this {@link Scope} will
     * be returned, otherwise all its {@link #parent()} will be inspected.
     * Finally, if no inspected {@link Scope} contains {@code name}, then {@link NoSuchVariableException}
     * is thrown
     *
     * @param name to get a value for
     * @return value that a variable holds, may be {@literal null}
     * @throws NoSuchVariableException if neither this scope contains a variable with given name, nor any of its parents
     * @throws IllegalArgumentException if {@code name == null}
     */
    Object getByName(String name);

    <T> T getByName(String name, Class<T> type);

    @CanNotIgnoreReturnValue
    Scope put(String name, Object value);

    boolean hasVariable(String name);

    @CanNotIgnoreReturnValue
    Scope declare(String name);

    @Nullable
    Scope parent();

    Scope newChild();

    void assign(String name, Object value);
}
