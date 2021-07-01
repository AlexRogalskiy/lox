package com.caco3.lox.environment;

import com.caco3.lox.util.Assert;
import com.caco3.lox.util.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleScope implements Scope {
    private static final Object NULL_SENTINEL = new Object();

    private final Map<String, Object> variables = new LinkedHashMap<>();
    @Nullable
    private final Scope parent;

    private SimpleScope(Scope parent) {
        this.parent = parent;
    }

    public static SimpleScope create() {
        return new SimpleScope(null);
    }

    private static SimpleScope createWithParent(Scope scope) {
        Assert.notNull(scope, "scope == null");
        return new SimpleScope(scope);
    }

    @Override
    public Object getByName(String name) {
        Assert.notNull(name, "name == null");

        Object object = variables.get(name);
        if (object != null) {
            return unmaskNull(object);
        }

        if (parent != null) {
            return parent.getByName(name);
        }
        throw NoSuchVariableException.forVariableName(name);
    }

    @Override
    public <T> T getByName(String name, Class<T> type) {
        Assert.notNull(name, "name == null");
        Assert.notNull(type, "type == null");

        return type.cast(getByName(name));
    }

    @Override
    public void put(String name, Object value) {
        Assert.notNull(name, "name == null");

        variables.put(name, maskNull(value));
    }


    @Override
    public boolean hasVariable(String name) {
        Assert.notNull(name, "name == null");
        return variables.containsKey(name);
    }

    @Override
    public void assign(String name, Object value) {
        Scope scope = findNearestScopeContaining(name);
        if (scope == null) {
            throw NoSuchVariableException.forVariableName(name);
        }
        scope.put(name, value);
    }

    private Scope findNearestScopeContaining(String name) {
        Assert.notNull(name, "name == null");
        Scope scope = this;
        while (scope != null) {
            if (scope.hasVariable(name)) {
                return scope;
            }
            scope = scope.parent();
        }
        return null;
    }

    @Override
    public Scope newChild() {
        return SimpleScope.createWithParent(this);
    }

    @Override
    public Scope parent() {
        return parent;
    }

    private static Object unmaskNull(Object o) {
        if (o == NULL_SENTINEL) {
            return null;
        }
        return o;
    }

    private static Object maskNull(Object o) {
        if (o == null) {
            return NULL_SENTINEL;
        }
        return o;
    }
}
