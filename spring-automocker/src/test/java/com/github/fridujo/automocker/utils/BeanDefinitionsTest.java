package com.github.fridujo.automocker.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BeanDefinitionsTest {

    @Test
    void extract_class_from_java_config_bean_definition() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);

        BeanDefinition beanDefinition = ((AnnotationConfigApplicationContext) context).getBeanDefinition("testBean");

        assertThat(BeanDefinitions.extractClass((AbstractBeanDefinition) beanDefinition)).isEqualTo(Stream.class);
    }

    @Test
    void extract_class_from_custom_bean_definition() {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(Stream.class);

        assertThat(BeanDefinitions.extractClass(beanDefinition)).isEqualTo(Stream.class);
    }

    @Test
    void extract_class_throws_when_beanclass_is_not_set() {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();

        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> BeanDefinitions.extractClass(beanDefinition))
            .withMessageContaining("Unable to resolve target class for BeanDefinition");
    }

    @Test
    void extract_class_throws_when_beanclass_is_not_in_classpath() {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClassName("missing.Someclass");

        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> BeanDefinitions.extractClass(beanDefinition))
            .withMessage("Cannot load class: missing.Someclass");
    }

    @Configuration
    public static class TestConfiguration {

        @Bean
        Stream<String> testBean() {
            return Stream.of("test");
        }
    }
}
