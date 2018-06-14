package com.github.fridujo.automocker.base;

import com.github.fridujo.automocker.api.AfterBeanRegistration;
import com.github.fridujo.automocker.api.AfterBeanRegistrationExecutable;
import com.github.fridujo.automocker.api.ExtendedBeanDefinitionRegistry;
import com.github.fridujo.automocker.api.tools.BeanLocator;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register utilities.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@AfterBeanRegistration(RegisterTools.RegisterToolsExecutable.class)
public @interface RegisterTools {

    class RegisterToolsExecutable implements AfterBeanRegistrationExecutable {

        @Override
        public void execute(Annotation annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            extendedBeanDefinitionRegistry.registerBeanDefinition(BeanLocator.class);
        }
    }
}
