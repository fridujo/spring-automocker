package com.github.fridujo.automocker.context;

import com.github.fridujo.automocker.api.ExtendedBeanDefinitionRegistry;
import com.github.fridujo.automocker.utils.BeanDefinitions;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.HashSet;
import java.util.Set;

class ExtendedBeanDefinitionRegistryImpl implements ExtendedBeanDefinitionRegistry {

    private final BeanDefinitionRegistry registry;

    ExtendedBeanDefinitionRegistryImpl(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Set<BeanDefinitionMetadata> getBeanDefinitionsForClass(Class<?> clazz) {
        Set<BeanDefinitionMetadata> result = new HashSet<>();

        for (String beanName : registry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            if (beanDefinition instanceof AbstractBeanDefinition) {
                Class<?> beanClass = BeanDefinitions.extractClass((AbstractBeanDefinition) beanDefinition);
                if (clazz.isAssignableFrom(beanClass)) {
                    result.add(new ImmutableBeanDefinitionMetadata(
                        this,
                        beanName,
                        beanClass,
                        (AbstractBeanDefinition) beanDefinition,
                        new BeanDefinitionModifier((AbstractBeanDefinition) beanDefinition)));
                }
            }
        }

        return result;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    private static class ImmutableBeanDefinitionMetadata implements BeanDefinitionMetadata {
        private final ExtendedBeanDefinitionRegistryImpl extendedBeanDefinitionRegistry;
        private final String name;
        private final Class<?> beanClass;
        private final AbstractBeanDefinition beanDefinition;
        private final BeanDefinitionModifier beanDefinitionModifier;

        private ImmutableBeanDefinitionMetadata(ExtendedBeanDefinitionRegistryImpl extendedBeanDefinitionRegistry, String name,
                                                Class<?> beanClass,
                                                AbstractBeanDefinition beanDefinition,
                                                BeanDefinitionModifier beanDefinitionModifier) {
            this.extendedBeanDefinitionRegistry = extendedBeanDefinitionRegistry;
            this.name = name;
            this.beanClass = beanClass;
            this.beanDefinition = beanDefinition;
            this.beanDefinitionModifier = beanDefinitionModifier;
        }


        @Override
        public String name() {
            return name;
        }

        @Override
        public Class<?> beanClass() {
            return beanClass;
        }

        @Override
        public AbstractBeanDefinition beanDefinition() {
            return beanDefinition;
        }

        @Override
        public BeanDefinitionModifier beanDefinitionModifier() {
            return beanDefinitionModifier;
        }

        @Override
        public String registerLinkedBeanDefinition(Class<?> beanClass) {
            String linkedBeanName = "Automocker" + name() + beanClass.getSimpleName();
            RootBeanDefinition linkedBeanDefinition = new RootBeanDefinition(beanClass);
            extendedBeanDefinitionRegistry.registerBeanDefinition(linkedBeanName, linkedBeanDefinition, getBeanQualifiers());
            return linkedBeanName;
        }
    }
}
