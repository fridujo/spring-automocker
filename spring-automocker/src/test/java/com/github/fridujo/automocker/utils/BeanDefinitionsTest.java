package com.github.fridujo.automocker.utils;

import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
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

    @Test
    void extractQualifier_for_simple_qualifier() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);

        BeanDefinition beanDefinition = ((AnnotationConfigApplicationContext) context).getBeanDefinition("test1_serializable");

        Map<String, Object> qualifiers = BeanDefinitions.extractQualifiers(beanDefinition);

        assertThat(qualifiers).containsOnly(MapEntry.entry(Qualifier.class.getName(), "test1"));
    }

    @Test
    void extractQualifier_for_scanned_quality_qualifier() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);

        BeanDefinition beanDefinition = ((AnnotationConfigApplicationContext) context).getBeanDefinition("test2_serializable");

        Map<String, Object> qualifiers = BeanDefinitions.extractQualifiers(beanDefinition);

        assertThat(qualifiers).containsOnly(MapEntry.entry(Platform.class.getName(), Platform.OperatingSystems.IOS));
    }

    @Test
    void extractQualifier_for_read_quality_qualifier() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);

        BeanDefinition beanDefinition = ((AnnotationConfigApplicationContext) context).getBeanDefinition("test3_serializable");

        Map<String, Object> qualifiers = BeanDefinitions.extractQualifiers(beanDefinition);

        assertThat(qualifiers).containsOnly(MapEntry.entry(Platform.class.getName(), Platform.OperatingSystems.ANDROID));
    }

    @Target({ElementType.FIELD,
        ElementType.METHOD,
        ElementType.TYPE,
        ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    public @interface Platform {

        OperatingSystems value();

        enum OperatingSystems {
            IOS,
            ANDROID
        }
    }

    @Configuration
    @ComponentScan
    public static class TestConfiguration {

        @Bean
        Stream<String> testBean() {
            return Stream.of("test");
        }

        @Bean
        @Qualifier("test1")
        Serializable test1_serializable() {
            return new Serializable() {
            };
        }

        @Bean
        @Platform(Platform.OperatingSystems.ANDROID)
        Serializable test3_serializable() {
            return new Serializable() {
            };
        }
    }

    @Configuration
    public static class SecondTestConfiguration {
        @Bean
        @Platform(Platform.OperatingSystems.IOS)
        Serializable test2_serializable() {
            return new Serializable() {
            };
        }
    }
}
