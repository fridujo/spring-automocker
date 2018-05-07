package com.github.fridujo.automocker.api;

import com.github.fridujo.automocker.utils.BeanDefinitions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.Map;
import java.util.Set;

public interface ExtendedBeanDefinitionRegistry {

    Set<BeanDefinitionMetadata> getBeanDefinitionsForClass(Class<?> clazz);

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    default String registerBeanDefinition(Class<?> beanClass) {
        String beanName = "Automocker" + beanClass.getSimpleName();
        registerBeanDefinition(beanName, new RootBeanDefinition(beanClass));
        return beanName;
    }

    default void registerBeanDefinition(String beanName, AbstractBeanDefinition abstractBeanDefinition, Map<String, Object> qualifiers) {
        qualifiers.forEach((qualifierType, qualifierValue) -> abstractBeanDefinition.addQualifier(new AutowireCandidateQualifier(qualifierType, qualifierValue)));
        registerBeanDefinition(beanName, abstractBeanDefinition);
    }

    interface BeanDefinitionMetadata {
        String name();

        Class<?> beanClass();

        AbstractBeanDefinition beanDefinition();

        BeanDefinitionModifier beanDefinitionModifier();

        default Map<String, Object> getBeanQualifiers() {
            return BeanDefinitions.extractQualifiers(beanDefinition());
        }

        String registerLinkedBeanDefinition(Class<?> beanClass);
    }

    class BeanDefinitionModifier {

        private static final String QUALIFIER_CLASS_NAME = Qualifier.class.getName();
        private static final String VALUE_FIELD_NAME = "value";

        private final AbstractBeanDefinition beanDefinition;

        public BeanDefinitionModifier(AbstractBeanDefinition beanDefinition) {
            this.beanDefinition = beanDefinition;
        }

        public BeanDefinitionModifier setFactoryBeanName(String factoryBeanName) {
            beanDefinition.setFactoryBeanName(factoryBeanName);
            return this;
        }

        public BeanDefinitionModifier setFactoryMethodName(String factoryMethodName) {
            beanDefinition.setFactoryMethodName(factoryMethodName);
            return this;
        }

        public BeanDefinitionModifier addConstructorIndexedArgumentValue(int index, Object value) {
            beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(index, value);
            return this;
        }

        public BeanDefinitionModifier setBeanClass(Class<?> beanClass) {
            beanDefinition.setBeanClass(beanClass);
            return this;
        }

        public BeanDefinitionModifier removeConstructorArgumentValues() {
            beanDefinition.setConstructorArgumentValues(null);
            return this;
        }

        public BeanDefinitionModifier removePropertyValues() {
            beanDefinition.setPropertyValues(null);
            return this;
        }

        public BeanDefinitionModifier addPropertyValue(String propertyName, Object propertyValue) {
            beanDefinition.getPropertyValues().add(propertyName, propertyValue);
            return this;
        }

        public BeanDefinitionModifier copyFactoryQualifiersAsDetached() {
            BeanDefinitions.extractQualifiers(beanDefinition)
                .forEach((qualifierType, qualifierValue) -> beanDefinition.addQualifier(new AutowireCandidateQualifier(qualifierType, qualifierValue)));
            return this;
        }

        public BeanDefinitionModifier reset() {
            setFactoryBeanName(null);
            setFactoryMethodName(null);
            removeConstructorArgumentValues();
            removePropertyValues();
            return this;
        }
    }
}
