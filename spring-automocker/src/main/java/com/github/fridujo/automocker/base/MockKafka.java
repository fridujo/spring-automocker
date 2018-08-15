package com.github.fridujo.automocker.base;

import com.github.fridujo.automocker.api.AfterBeanRegistration;
import com.github.fridujo.automocker.api.AfterBeanRegistrationExecutable;
import com.github.fridujo.automocker.api.ExtendedBeanDefinitionRegistry;
import com.github.fridujo.automocker.api.kafka.MockProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@AfterBeanRegistration(MockKafka.MockKafkaReconfiguration.class)
public @interface MockKafka {
    class MockKafkaReconfiguration implements AfterBeanRegistrationExecutable<MockKafka> {

        @Override
        public void execute(MockKafka annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            Set<ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata> producerFactoryBeans = extendedBeanDefinitionRegistry
                .getBeanDefinitionsForClass(ProducerFactory.class);

            producerFactoryBeans.forEach(meta -> {
                meta.beanDefinitionModifier()
                    .copyFactoryQualifiersAsDetached()
                    .reset()
                    .setBeanClass(MockProducerFactory.class);
            });
        }
    }
}
