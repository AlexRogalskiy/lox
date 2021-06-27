package com.caco3.lox.environment;

import com.caco3.lox.util.Assert;
import com.caco3.lox.util.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapEnvironment implements Environment {
    private static final Object NULL_SENTINEL = new Object();

    private final Map<String, Object> variables = new LinkedHashMap<>();
    @Nullable
    private final Environment parent;

    private MapEnvironment(Environment parent) {
        this.parent = parent;
    }

    public static MapEnvironment create() {
        return new MapEnvironment(null);
    }

    public static MapEnvironment createWithParent(Environment environment) {
        Assert.notNull(environment, "environment == null");
        return new MapEnvironment(environment);
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
    public Environment parent() {
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
