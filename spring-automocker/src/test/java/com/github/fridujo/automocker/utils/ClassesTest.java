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

    public static class Instanciable {

    }

    private static class Uninstanciable {

    }
}
