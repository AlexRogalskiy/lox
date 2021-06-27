package com.caco3.lox.environment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnvironmentTest {
    private final Environment environment = MapEnvironment.create();

    @Test
    void variablePutAndGet() {
        int value = 1;
        String name = "abc";

        environment.put(name, value);

        assertThat(environment.getByName(name))
                .isEqualTo(value);
    }

    @Test
    void variablePutAndGetWithClass() {
        int value = 1;
        String name = "abc";

        environment.put(name, value);

        assertThat(environment.getByName(name, Integer.class))
                .isEqualTo(value);
    }

    @Test
    void nullSuccessfullyStored() {
        environment.put("def", null);

        assertThat(environment.getByName("def"))
                .isNull();
    }

    @Test
    void nullSuccessfullyStoredAndRetrievedFromCurrentEnvironment() {
        environment.put("abc", 3);
        MapEnvironment newScopedEnvironment = MapEnvironment.createWithParent(environment);
        newScopedEnvironment.put("abc", null);

        assertThat(newScopedEnvironment.getByName("abc"))
                .isNull();
    }

    @Test
    void parentIsConsulted() {
        String name = "abc";
        int value = 42;
        environment.put(name, value);

        MapEnvironment newScopedEnvironment = MapEnvironment.createWithParent(environment);

        assertThat(newScopedEnvironment.getByName(name))
                .isEqualTo(value);
    }

    @Test
    void throwsWhenNonExistentVariableIsAsked() {
        assertThatThrownBy(() -> environment.getByName("def"))
                .isInstanceOf(NoSuchVariableException.class);
    }

    @Test
    void throwsIfNameIsNullWhenPutting() {
        assertThatThrownBy(() -> environment.put(null, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwsIfNameIsNullWhenGetting() {
        assertThatThrownBy(() -> environment.getByName(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwsIfNameIsNullWhenGettingWithClass() {
        assertThatThrownBy(() -> environment.getByName(null, Integer.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwsIfClassIsNullWhenGettingWithClass() {
        assertThatThrownBy(() -> environment.getByName("abc", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}