package com.github.fridujo.automocker.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ClassesTest {

    @Test
    void successful_instanciation() {
        Instanciable instance = Classes.instanciate(Instanciable.class);

        assertThat(instance).isExactlyInstanceOf(Instanciable.class);
    }

    @Test
    void failed_instanciation() {
        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> Classes.instanciate(Uninstanciable.class))
            .withMessage("Cannot instanciate class " +
                Uninstanciable.class.getName() +
                ": Class " + Classes.class.getName() +
                " can not access a member of class " +
                Uninstanciable.class.getName() +
                " with modifiers \"private\""
            );
    }

    @Test
    void isPresent_returns_true_when_class_is_on_classpath() {
        assertThat(Classes.isPresent("java.lang.String")).isTrue();
    }

    @Test
    void isPresent_returns_false_when_class_is_not_on_classpath() {
        assertThat(Classes.isPresent("missing.Someclass")).isFalse();
    }

    public static class Instanciable {

    }

    private static class Uninstanciable {

    }
}
