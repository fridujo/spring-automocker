package com.github.fridujo.automocker.utils;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ThrowingFunctionTest {

    Function<Mode, String> silenced = ThrowingFunction.silent(this::doNotThrow);

    @Test
    void silenced_function_when_not_throwing() {
        assertThat(silenced.apply(Mode.DO_NOT_THROW)).isEqualTo("test");
    }

    @Test
    void silenced_function_when_throwing() {
        assertThatExceptionOfType(LamdaLuggageException.class)
            .isThrownBy(() -> silenced.apply(Mode.THROW_CHECKED))
            .withCauseExactlyInstanceOf(TestException.class);
    }

    @Test
    void silenced_function_when_throwing_unchecked() {
        assertThatExceptionOfType(TestRuntimeException.class)
            .isThrownBy(() -> silenced.apply(Mode.THROW_UNCHECKED))
            .withCause(null);
    }

    private String doNotThrow(Mode input) throws Exception {
        if (Mode.DO_NOT_THROW == input) {
            return "test";
        } else if (Mode.THROW_CHECKED == input) {
            throw new TestException();
        } else {
            throw new TestRuntimeException();
        }
    }

    private enum Mode {
        DO_NOT_THROW, THROW_CHECKED, THROW_UNCHECKED;
    }

    private static class TestException extends Exception {
    }

    private static class TestRuntimeException extends RuntimeException {
    }
}
