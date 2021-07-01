package com.caco3.lox.environment;

import com.caco3.lox.util.Assert;
import com.caco3.lox.util.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class SimpleScope implements Scope {
    private static final Object NULL_SENTINEL = new Object();
    private static final Object DECLARATION_SENTINEL = new Object();

    private final Map<String, Object> variables;
    @Nullable
    private final Scope parent;

    private SimpleScope(Scope parent) {
        this(new LinkedHashMap<>(), parent);
    }

    private SimpleScope(Map<String, Object> variables, Scope parent) {
        Assert.notNull(variables, "variables == null");
        this.variables = variables;
        this.parent = parent;

        logger.trace("Created new scope, variables = {}, parent = {}", variables, parent);
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
    public Scope put(String name, Object value) {
        Assert.notNull(name, "name == null");

        Map<String, Object> newVariables = new LinkedHashMap<>(variables);
        newVariables.put(name, maskNull(value));
        return new SimpleScope(newVariables, parent);
    }


    @Override
    public boolean hasVariable(String name) {
        Assert.notNull(name, "name == null");
        return variables.containsKey(name);
    }

    @Override
    public Scope declare(String name) {
        return put(name, DECLARATION_SENTINEL);
    }

    @Override
    public void assign(String name, Object value) {
        if (variables.containsKey(name)) {
            variables.put(name, value);
        } else if (parent != null) {
            parent.assign(name, value);
        } else {
            throw NoSuchVariableException.forVariableName(name);
        }
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
