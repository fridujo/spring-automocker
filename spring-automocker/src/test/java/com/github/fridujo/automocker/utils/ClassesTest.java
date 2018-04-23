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

    @Test
    void forName_returns_class_object_when_present_on_classpath() {
        assertThat(Classes.forName("java.lang.String")).isEqualTo(String.class);
    }

    @Test
    void forName_throws_when_class_is_not_on_classpath() {
        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> Classes.forName("missing.Someclass"))
            .withMessage("Cannot load class missing.Someclass");
    }

    @Test
    void getValueFromProtectedField_when_field_exists() {
        Object object = new Instanciable();
        String existingFieldValue = Classes.getValueFromProtectedField(object, "existingField");
        assertThat(existingFieldValue).as("Existing field value").isEqualTo("test");
    }

    @Test
    void getValueFromProtectedField_when_field_does_not_exist() {
        Object object = new Instanciable();
        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> Classes.getValueFromProtectedField(object, "missingField"))
            .withMessage("Cannot find field missingField in class com.github.fridujo.automocker.utils.ClassesTest$Instanciable");
    }

    public static class Instanciable {
        private final String existingField = "test";
    }

    private static class Uninstanciable {
    }
}
