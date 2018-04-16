package com.github.fridujo.automocker.utils;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.ClassUtils;

public final class BeanDefinitions {

    private static final ClassLoader BEAN_CLASS_LOADER = ClassUtils.getDefaultClassLoader();

    private BeanDefinitions() {
    }

    public static Class<?> extractClass(AbstractBeanDefinition beanDefinition) {
        AbstractBeanDefinition abd = beanDefinition;
        try {
            Class<?> beanClazz = abd.resolveBeanClass(BEAN_CLASS_LOADER);
            MethodMetadata factoryMetadata = getMethodMetadata(beanDefinition);
            if (beanClazz != null) {
                return beanClazz;
            } else if (factoryMetadata != null) {
                return Class.forName(factoryMetadata.getReturnTypeName());
            } else {
                throw new IllegalStateException(
                    "Unable to resolve target class for BeanDefinition [" + beanDefinition + "]");
            }
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Cannot load class: " + ex.getMessage(), ex);
        }
    }

    private static MethodMetadata getMethodMetadata(BeanDefinition beanDef) {
        return AnnotatedBeanDefinition.class.isAssignableFrom(beanDef.getClass())
            ? ((AnnotatedBeanDefinition) beanDef).getFactoryMethodMetadata() : null;
    }
}
