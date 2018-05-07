package com.github.fridujo.automocker.base;

import com.github.fridujo.automocker.api.AfterBeanRegistration;
import com.github.fridujo.automocker.api.AfterBeanRegistrationExecutable;
import com.github.fridujo.automocker.api.ExtendedBeanDefinitionRegistry;
import com.github.fridujo.automocker.utils.Classes;
import com.github.fridujo.rabbitmq.mock.spring.MockConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

/**
 * Alter bean definitions of {@link ConnectionFactory ConnectionFactories} by changing bean class with {@link MockConnectionFactory}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@AfterBeanRegistration(MockAmqp.MockAmqpExecutable.class)
public @interface MockAmqp {

    class MockAmqpExecutable implements AfterBeanRegistrationExecutable<MockAmqp> {

        @Override
        public void execute(MockAmqp annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            if (Classes.isPresent("org.springframework.amqp.rabbit.connection.ConnectionFactory")) {
                Set<ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata> connectionFactoryBeans = extendedBeanDefinitionRegistry
                    .getBeanDefinitionsForClass(ConnectionFactory.class);
                if (connectionFactoryBeans.size() > 0) {
                    connectionFactoryBeans.forEach(bdm -> bdm.beanDefinitionModifier()
                        .copyFactoryQualifiersAsDetached()
                        .reset()
                        .setBeanClass(MockConnectionFactory.class)
                    );
                }
            }
        }
    }
}
