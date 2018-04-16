package com.github.fridujo.automocker.context;

import com.github.fridujo.automocker.api.AfterBeanRegistration;
import com.github.fridujo.automocker.api.BeforeBeanRegistration;
import com.github.fridujo.automocker.utils.Annotations;
import com.github.fridujo.automocker.utils.Classes;
import com.github.fridujo.automocker.utils.Version;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.util.Set;

class AutomockerPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    static final String BEAN_NAME = AutomockerPostProcessor.class.getName();
    static final Version SPRING_MIN_VERSION = Version.major(4).minor(3);

    private final Class<?> testClass;

    AutomockerPostProcessor(Class<?> testClass, ConfigurableApplicationContext context) {
        this.testClass = testClass;
        checkSpringVersion();
        invokeBeforeBeanRegistrationHooks(context);
    }

    private void checkSpringVersion() {
        Version springVersion = Version.spring();
        if (springVersion.isBefore(SPRING_MIN_VERSION)) {
            throw new IllegalStateException("Automocker needs Spring in version >= " + SPRING_MIN_VERSION + ", found " + springVersion);
        }
    }

    private void invokeBeforeBeanRegistrationHooks(ConfigurableApplicationContext context) {
        Set<Annotations.AnnotatedAnnotation<BeforeBeanRegistration>> beforeBeanRegistrationAnnotations
            = Annotations.getAnnotationsAnnotatedWith(testClass, BeforeBeanRegistration.class);
        beforeBeanRegistrationAnnotations
            .forEach(annotatedAnnotation -> Classes
                .instanciate(annotatedAnnotation.parentAnnotation().value())
                .execute(annotatedAnnotation.annotation(), context)
            );
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
        throws BeansException {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
        throws BeansException {
        ExtendedBeanDefinitionRegistryImpl extendedBeanDefinitionRegistry = new ExtendedBeanDefinitionRegistryImpl(registry);

        Set<Annotations.AnnotatedAnnotation<AfterBeanRegistration>> beforeBeanRegistrationAnnotations
            = Annotations.getAnnotationsAnnotatedWith(testClass, AfterBeanRegistration.class);
        beforeBeanRegistrationAnnotations.forEach(annotatedAnnotation -> Classes
            .instanciate(annotatedAnnotation.parentAnnotation().value())
            .execute(annotatedAnnotation.annotation(), extendedBeanDefinitionRegistry)
        );
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
