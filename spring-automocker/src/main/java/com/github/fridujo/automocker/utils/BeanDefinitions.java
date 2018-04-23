package com.github.fridujo.automocker.utils;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.core.type.classreading.MethodMetadataReadingVisitor;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class BeanDefinitions {

    private static final ClassLoader BEAN_CLASS_LOADER = ClassUtils.getDefaultClassLoader();
    private static final String QUALIFIER_CLASS_NAME = Qualifier.class.getName();
    private static final String VALUE_FIELD_NAME = "value";

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

    public static Map<String, Object> extractQualifiers(BeanDefinition beanDefinition) {
        Map<String, Object> qualifiers = new LinkedHashMap<>();
        if (beanDefinition instanceof AnnotatedBeanDefinition) {
            MethodMetadata factoryMethodMetadata = ((AnnotatedBeanDefinition) beanDefinition).getFactoryMethodMetadata();
            if (factoryMethodMetadata instanceof MethodMetadataReadingVisitor) {
                Map<String, Object> qualityQualifiers = listQualityQualifiers((MethodMetadataReadingVisitor) factoryMethodMetadata);
                qualifiers.putAll(qualityQualifiers);
            } else if (factoryMethodMetadata instanceof StandardMethodMetadata) {
                Map<String, Object> qualityQualifiers = listQualityQualifiers((StandardMethodMetadata) factoryMethodMetadata);
                qualifiers.putAll(qualityQualifiers);
            }

            Map<String, Object> annotationAttributes = factoryMethodMetadata.getAnnotationAttributes(QUALIFIER_CLASS_NAME);
            Object qualifierValue = annotationAttributes.get(VALUE_FIELD_NAME);
            if (!"".equals(qualifierValue)) {
                qualifiers.put(QUALIFIER_CLASS_NAME, qualifierValue);
            }
        }
        return qualifiers;
    }

    private static Map<String, Object> listQualityQualifiers(MethodMetadataReadingVisitor factoryMethodMetadata) {
        // TODO make a PR to Spring to add a method in AnnotatedTypeMetadata for annotation search
        Map<String, Set<String>> metaAnnotationMap = Classes.getValueFromProtectedField(factoryMethodMetadata, "metaAnnotationMap");
        Map<String, Object> qualityQualifiers = new HashMap<>();
        for (Map.Entry<String, Set<String>> annotationData : metaAnnotationMap.entrySet()) {
            if (annotationData.getValue().contains(QUALIFIER_CLASS_NAME)) {
                Object qualifierValue = factoryMethodMetadata.getAnnotationAttributes(annotationData.getKey()).get(VALUE_FIELD_NAME);
                if (qualifierValue != null) {
                    qualityQualifiers.put(annotationData.getKey(), qualifierValue);
                }
            }
        }
        return qualityQualifiers;
    }

    private static Map<String, Object> listQualityQualifiers(StandardMethodMetadata factoryMethodMetadata) {
        Set<Annotations.AnnotatedAnnotation<Qualifier>> annotationsAnnotatedWithQualifier = Annotations.getAnnotationsAnnotatedWith(factoryMethodMetadata.getIntrospectedMethod(), Qualifier.class);
        Map<String, Object> qualityQualifiers = new HashMap<>();
        annotationsAnnotatedWithQualifier.forEach(annotationAnnotatedWithQualifier -> {
            Annotation qualityQualifier = annotationAnnotatedWithQualifier.annotation();
            Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(qualityQualifier);
            if (annotationAttributes.containsKey(VALUE_FIELD_NAME)) {
                qualityQualifiers.put(qualityQualifier.annotationType().getName(), annotationAttributes.get(VALUE_FIELD_NAME));
            }
        });
        return qualityQualifiers;
    }
}
