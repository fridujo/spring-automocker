package com.github.fridujo.automocker.context;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;

class AutomockerContextCustomizer implements ContextCustomizer {

    private final Class<?> testClass;

    AutomockerContextCustomizer(Class<?> testClass) {
        this.testClass = testClass;
    }

    @Override
    public void customizeContext(ConfigurableApplicationContext context,
                                 MergedContextConfiguration mergedContextConfiguration) {
        BeanDefinitionRegistry registry = getBeanDefinitionRegistry(context);
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(
            registry);
        registerCleanupPostProcessor(registry, reader, context);
    }

    private void registerCleanupPostProcessor(BeanDefinitionRegistry registry,
                                              AnnotatedBeanDefinitionReader reader, ConfigurableApplicationContext context) {
        BeanDefinition definition = registerBean(registry, reader,
            AutomockerPostProcessor.BEAN_NAME, AutomockerPostProcessor.class);
        definition.getConstructorArgumentValues().addIndexedArgumentValue(0,
            this.testClass);
        definition.getConstructorArgumentValues().addIndexedArgumentValue(1,
            context);

    }

    private BeanDefinitionRegistry getBeanDefinitionRegistry(ApplicationContext context) {
        if (context instanceof BeanDefinitionRegistry) {
            return (BeanDefinitionRegistry) context;
        }
        if (context instanceof AbstractApplicationContext) {
            return (BeanDefinitionRegistry) ((AbstractApplicationContext) context)
                .getBeanFactory();
        }
        throw new IllegalStateException("Could not locate BeanDefinitionRegistry");
    }

    @SuppressWarnings("unchecked")
    private BeanDefinition registerBean(BeanDefinitionRegistry registry,
                                        AnnotatedBeanDefinitionReader reader, String beanName, Class<?> type) {
        reader.registerBean(type, beanName);
        BeanDefinition definition = registry.getBeanDefinition(beanName);
        return definition;
    }

    @Override
    public boolean equals(Object other) {
        return (this == other || (other != null && getClass() == other.getClass()));
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
