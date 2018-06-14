package com.github.fridujo.automocker.base;

import com.github.fridujo.automocker.api.AfterBeanRegistration;
import com.github.fridujo.automocker.api.AfterBeanRegistrationExecutable;
import com.github.fridujo.automocker.api.ExtendedBeanDefinitionRegistry;
import com.github.fridujo.automocker.utils.Classes;
import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Alter bean definitions of {@link ConnectionFactory ConnectionFactories} by changing bean class with {@link MockConnectionFactory}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@AfterBeanRegistration(MockAmqp.MockAmqpExecutable.class)
public @interface MockAmqp {

    class MockAmqpExecutable implements AfterBeanRegistrationExecutable<MockAmqp> {

        private static final String RABBITMQ_MOCK_CONNECTION_FACTORY_CLASS = "com.github.fridujo.rabbitmq.mock.MockConnectionFactory";

        @Override
        public void execute(MockAmqp annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            if (Classes.isPresent("org.springframework.amqp.rabbit.connection.ConnectionFactory")) {
                modifyConnectionFactoryBeanDefinitions(extendedBeanDefinitionRegistry);
            }
        }

        private void modifyConnectionFactoryBeanDefinitions(ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            Class<ConnectionFactory> clazzToMock = ConnectionFactory.class;
            Set<ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata> connectionFactoryBeans = extendedBeanDefinitionRegistry
                .getBeanDefinitionsForClass(clazzToMock);
            if (!connectionFactoryBeans.isEmpty()) {
                if (Classes.isPresent(RABBITMQ_MOCK_CONNECTION_FACTORY_CLASS)) {
                    connectionFactoryBeans.forEach(meta -> {

                        String mockConnectionFactoryBeanName = extendedBeanDefinitionRegistry.registerBeanDefinition(
                            "Automocker" + meta.name() + "MockConnectionFactory",
                            new RootBeanDefinition(MockConnectionFactory.class),
                            meta.getBeanQualifiers());

                        meta.beanDefinitionModifier()
                            .copyFactoryQualifiersAsDetached()
                            .reset()
                            .setBeanClass(CachingConnectionFactory.class)
                            .addConstructorIndexedArgumentValue(0, new RuntimeBeanReference(mockConnectionFactoryBeanName));
                    });
                } else {
                    throw new IllegalStateException("\nAutomocker is missing class [" + RABBITMQ_MOCK_CONNECTION_FACTORY_CLASS + "] to mock " + connectionFactoryBeans.size() + " bean(s) of type [" + clazzToMock.getName() + "]: " +
                        connectionFactoryBeans.stream().map(ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata::name).collect(Collectors.joining(", ")) +
                        "\nMake sure rabbitmq-mock.jar is in the test classpath");
                }
            }
        }
    }
}
