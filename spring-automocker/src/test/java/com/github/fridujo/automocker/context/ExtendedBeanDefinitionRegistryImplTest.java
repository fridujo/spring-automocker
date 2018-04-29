package com.github.fridujo.automocker.context;

import com.github.fridujo.automocker.api.ExtendedBeanDefinitionRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExtendedBeanDefinitionRegistryImplTest {

    @Test
    void list_bean_definitions_by_type() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ExtendedBeanDefinitionRegistryImplTest.TestConfiguration.class);
        ExtendedBeanDefinitionRegistry extendedRegistry = new ExtendedBeanDefinitionRegistryImpl(context);

        Set<ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata> beanDefinitions = extendedRegistry.getBeanDefinitionsForClass(Stream.class);

        assertThat(beanDefinitions)
            .hasOnlyOneElementSatisfying(beanDefinitionMetadata -> {
                assertThat(beanDefinitionMetadata.name()).isEqualTo("testBean");
                assertThat(beanDefinitionMetadata.beanClass()).isEqualTo(Stream.class);
                assertThat(beanDefinitionMetadata.beanDefinition()).isNotNull();
                assertThat(beanDefinitionMetadata.beanDefinitionModifier()).isNotNull();
            });
    }

    @Test
    void register_bean_definition_delegates() {
        BeanDefinitionRegistry delegate = mock(BeanDefinitionRegistry.class);
        ExtendedBeanDefinitionRegistry extendedRegistry = new ExtendedBeanDefinitionRegistryImpl(delegate);

        extendedRegistry.registerBeanDefinition("test", null);

        verify(delegate, times(1)).registerBeanDefinition(any(), any());
    }

    @Test
    void register_linked_bean_definition_delegates() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ExtendedBeanDefinitionRegistryImplTest.TestConfiguration.class);
        ExtendedBeanDefinitionRegistry extendedRegistry = new ExtendedBeanDefinitionRegistryImpl(context);
        Set<ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata> beanDefinitions = extendedRegistry.getBeanDefinitionsForClass(Stream.class);

        assertThat(beanDefinitions).hasSize(1);

        ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata metadata = beanDefinitions.iterator().next();

        String linkedBeanName = metadata.registerLinkedBeanDefinition(String.class);
        AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) context.getBeanDefinition(linkedBeanName);

        assertThat(linkedBeanName).contains(metadata.name());
        assertThat(beanDefinition.getBeanClassName()).isEqualTo(String.class.getName());
        assertThat(beanDefinition.getQualifiers()).hasSize(1);
        AutowireCandidateQualifier qualifier = beanDefinition.getQualifiers().iterator().next();
        assertThat(qualifier.getTypeName()).isEqualTo(Qualifier.class.getName());
        assertThat(qualifier.getAttribute(AutowireCandidateQualifier.VALUE_KEY)).isEqualTo("testQ");
    }

    @Configuration
    public static class TestConfiguration {

        @Bean
        @Qualifier("testQ")
        Stream<String> testBean() {
            return Stream.of("test");
        }
    }
}
