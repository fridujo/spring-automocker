package com.github.fridujo.automocker.utils;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ThrowingTest {

    private static String behave(Behavior input) throws Exception {
        if (Behavior.DO_NOT_THROW == input) {
            return "test";
        } else if (Behavior.THROW_CHECKED == input) {
            throw new TestException();
        } else {
            throw new TestRuntimeException();
        }
    }

    private enum Behavior {
        DO_NOT_THROW, THROW_CHECKED, THROW_UNCHECKED
    }

    @Nested
    static class ThrowingFunctionTest {
        Function<Behavior, String> silenced = ThrowingFunction.silent(ThrowingTest::behave);

        @Test
        void silenced_function_when_not_throwing() {
            assertThat(silenced.apply(Behavior.DO_NOT_THROW)).isEqualTo("test");
        }

        @Test
        void silenced_function_when_throwing() {
            assertThatExceptionOfType(LamdaLuggageException.class)
                .isThrownBy(() -> silenced.apply(Behavior.THROW_CHECKED))
                .withCauseExactlyInstanceOf(TestException.class);
        }

        @Test
        void silenced_function_when_throwing_unchecked() {
            assertThatExceptionOfType(TestRuntimeException.class)
                .isThrownBy(() -> silenced.apply(Behavior.THROW_UNCHECKED))
                .withCause(null);
        }
    }

    @Nested
    static class ThrowingConsumerTest {
        Consumer<Behavior> silenced = ThrowingConsumer.silent(ThrowingTest::behave);

        @Test
        void silenced_consumer_when_not_throwing() {
            silenced.accept(Behavior.DO_NOT_THROW);
        }

        @Test
        void silenced_function_when_throwing() {
            assertThatExceptionOfType(LamdaLuggageException.class)
                .isThrownBy(() -> silenced.accept(Behavior.THROW_CHECKED))
                .withCauseExactlyInstanceOf(TestException.class);
        }

        @Test
        void silenced_function_when_throwing_unchecked() {
            assertThatExceptionOfType(TestRuntimeException.class)
                .isThrownBy(() -> silenced.accept(Behavior.THROW_UNCHECKED))
                .withCause(null);
        }
    }

    @Nested
    static class ThrowingBiConsumerTest {
        BiConsumer<Behavior, String> silenced = ThrowingBiConsumer.silent(ThrowingBiConsumerTest::behave);

        private static String behave(Behavior input, String secondParameter) throws Exception {
            if (Behavior.DO_NOT_THROW == input) {
                return "test";
            } else if (Behavior.THROW_CHECKED == input) {
                throw new TestException();
            } else {
                throw new TestRuntimeException();
            }
        }

        @Test
        void silenced_consumer_when_not_throwing() {
            silenced.accept(Behavior.DO_NOT_THROW, null);
        }

        @Test
        void silenced_function_when_throwing() {
            assertThatExceptionOfType(LamdaLuggageException.class)
                .isThrownBy(() -> silenced.accept(Behavior.THROW_CHECKED, null))
                .withCauseExactlyInstanceOf(TestException.class);
        }

        @Test
        void silenced_function_when_throwing_unchecked() {
            assertThatExceptionOfType(TestRuntimeException.class)
                .isThrownBy(() -> silenced.accept(Behavior.THROW_UNCHECKED, null))
                .withCause(null);
        }
    }

    private static class TestException extends Exception {
    }

    private static class TestRuntimeException extends RuntimeException {
    }
}
