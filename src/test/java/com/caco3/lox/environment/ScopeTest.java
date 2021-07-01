package com.caco3.lox.environment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScopeTest {
    @Test
    void variablePutAndGet() {
        int value = 1;
        String name = "abc";

        Scope scope = SimpleScope.create().put(name, value);

        assertThat(scope.getByName(name))
                .isEqualTo(value);
    }

    @Test
    void variablePutAndGetWithClass() {
        int value = 1;
        String name = "abc";

        Scope scope = SimpleScope.create().put(name, value);

        assertThat(scope.getByName(name, Integer.class))
                .isEqualTo(value);
    }

    @Test
    void nullSuccessfullyStored() {
        Scope scope = SimpleScope.create().put("def", null);

        assertThat(scope.getByName("def"))
                .isNull();
    }

    @Test
    void nullSuccessfullyStoredAndRetrievedFromCurrentEnvironment() {
        Scope scope = SimpleScope.create().put("abc", 3);
        Scope newScopedEnvironment = scope.newChild().put("abc", null);

        assertThat(newScopedEnvironment.getByName("abc"))
                .isNull();
    }

    @Test
    void parentIsConsulted() {
        String name = "abc";
        int value = 42;
        Scope scope = SimpleScope.create().put(name, value);

        Scope newScopedEnvironment = scope.newChild();

        assertThat(newScopedEnvironment.getByName(name))
                .isEqualTo(value);
    }

    @Test
    void variableAssignedToValue() {
        String name = "abc";
        Scope scope = SimpleScope.create().put(name, 32);

        int newValue = 52;
        scope.assign(name, newValue);

        assertThat(scope.getByName(name))
                .isEqualTo(newValue);
    }

    @Test
    void variableAssignedInParentEnvironment() {
        String name = "abc";
        Scope parent = SimpleScope.create().put(name, 42);
        Scope newScopedEnvironment = parent.newChild();

        int newValue = 34;
        newScopedEnvironment.assign(name, newValue);

        assertThat(parent.getByName(name))
                .isEqualTo(newValue);
    }


    @Test
    void throwsWhenNonExistentVariableIsAsked() {
        assertThatThrownBy(() -> SimpleScope.create().getByName("def"))
                .isInstanceOf(NoSuchVariableException.class);
    }

    @Test
    void throwsIfNameIsNullWhenPutting() {
        assertThatThrownBy(() -> SimpleScope.create().put(null, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwsIfNameIsNullWhenGetting() {
        assertThatThrownBy(() -> SimpleScope.create().getByName(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwsIfNameIsNullWhenGettingWithClass() {
        assertThatThrownBy(() -> SimpleScope.create().getByName(null, Integer.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwsIfClassIsNullWhenGettingWithClass() {
        assertThatThrownBy(() -> SimpleScope.create().getByName("abc", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}