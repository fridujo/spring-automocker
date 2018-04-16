package com.github.fridujo.automocker.context;

import com.github.fridujo.automocker.api.ExtendedBeanDefinitionRegistry;
import org.junit.jupiter.api.Test;
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

    @Configuration
    public static class TestConfiguration {

        @Bean
        Stream<String> testBean() {
            return Stream.of("test");
        }
    }
}
