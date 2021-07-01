package com.caco3.lox.environment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScopeTest {
    private final Scope scope = SimpleScope.create();

    @Test
    void variablePutAndGet() {
        int value = 1;
        String name = "abc";

        scope.put(name, value);

        assertThat(scope.getByName(name))
                .isEqualTo(value);
    }

    @Test
    void variablePutAndGetWithClass() {
        int value = 1;
        String name = "abc";

        scope.put(name, value);

        assertThat(scope.getByName(name, Integer.class))
                .isEqualTo(value);
    }

    @Test
    void nullSuccessfullyStored() {
        scope.put("def", null);

        assertThat(scope.getByName("def"))
                .isNull();
    }

    @Test
    void nullSuccessfullyStoredAndRetrievedFromCurrentEnvironment() {
        scope.put("abc", 3);
        Scope newScopedEnvironment = scope.newChild();
        newScopedEnvironment.put("abc", null);

        assertThat(newScopedEnvironment.getByName("abc"))
                .isNull();
    }

    @Test
    void parentIsConsulted() {
        String name = "abc";
        int value = 42;
        scope.put(name, value);

        Scope newScopedEnvironment = scope.newChild();

        assertThat(newScopedEnvironment.getByName(name))
                .isEqualTo(value);
    }

    @Test
    void variableAssignedToValue() {
        String name = "abc";
        scope.put(name, 32);
        int newValue = 52;
        scope.assign(name, newValue);

        assertThat(scope.getByName(name))
                .isEqualTo(newValue);
    }

    @Test
    void variableAssignedInParentEnvironment() {
        Scope parent = scope;
        Scope newScopedEnvironment = parent.newChild();
        String name = "abc";

        parent.put(name, 42);
        int newValue = 34;
        newScopedEnvironment.assign(name, newValue);

        assertThat(parent.getByName(name))
                .isEqualTo(newValue);
    }


    @Test
    void throwsWhenNonExistentVariableIsAsked() {
        assertThatThrownBy(() -> scope.getByName("def"))
                .isInstanceOf(NoSuchVariableException.class);
    }

    @Test
    void throwsIfNameIsNullWhenPutting() {
        assertThatThrownBy(() -> scope.put(null, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwsIfNameIsNullWhenGetting() {
        assertThatThrownBy(() -> scope.getByName(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwsIfNameIsNullWhenGettingWithClass() {
        assertThatThrownBy(() -> scope.getByName(null, Integer.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwsIfClassIsNullWhenGettingWithClass() {
        assertThatThrownBy(() -> scope.getByName("abc", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}