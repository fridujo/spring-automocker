package com.github.fridujo.automocker.base;

import com.github.fridujo.automocker.api.AfterBeanRegistration;
import com.github.fridujo.automocker.api.AfterBeanRegistrationExecutable;
import com.github.fridujo.automocker.api.ExtendedBeanDefinitionRegistry;
import com.github.fridujo.automocker.api.jms.DestinationManagerResetter;
import com.github.fridujo.automocker.api.jms.JmsListenerContainerFactoryConfigurer;
import com.github.fridujo.automocker.api.jms.JmsMock;
import com.github.fridujo.automocker.api.jms.JmsMockLocator;
import com.github.fridujo.automocker.utils.Classes;
import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockConnectionFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@AfterBeanRegistration(MockJms.MockJmsExecutable.class)
public @interface MockJms {

    class MockJmsExecutable implements AfterBeanRegistrationExecutable<MockJms> {
        private static final String MOCKRUNNER_CONNECTION_FACTORY_CLASS = "com.mockrunner.mock.jms.MockConnectionFactory";

        @Override
        public void execute(MockJms annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            if (Classes.isPresent("javax.jms.ConnectionFactory")) {
                modifyConnectionFactoryBeanDefinitions(extendedBeanDefinitionRegistry);
            }
        }

        private void modifyConnectionFactoryBeanDefinitions(ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            Set<ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata> connectionFactoryBeans = extendedBeanDefinitionRegistry
                .getBeanDefinitionsForClass(ConnectionFactory.class);

            if (!connectionFactoryBeans.isEmpty()) {
                if (Classes.isPresent(MOCKRUNNER_CONNECTION_FACTORY_CLASS)) {
                    connectionFactoryBeans.forEach(meta -> {
                        String destinationManagerBeanName = "Automocker" + meta.name() + "DestinationManager";
                        String configurationManagerBeanName = "Automocker" + meta.name() + "ConfigurationManager";
                        String jmsMockBeanName = "Automocker" + meta.name() + "JmsMock";

                        Map<String, Object> qualifiers = meta.getBeanQualifiers();

                        meta.beanDefinitionModifier()
                            .copyFactoryQualifiersAsDetached()
                            .reset()
                            .setBeanClass(MockConnectionFactory.class)
                            .addConstructorIndexedArgumentValue(0, new RuntimeBeanReference(destinationManagerBeanName))
                            .addConstructorIndexedArgumentValue(1, new RuntimeBeanReference(configurationManagerBeanName));


                        extendedBeanDefinitionRegistry.registerBeanDefinition(destinationManagerBeanName, new RootBeanDefinition(DestinationManager.class), qualifiers);
                        extendedBeanDefinitionRegistry.registerBeanDefinition(configurationManagerBeanName, new RootBeanDefinition(ConfigurationManager.class), qualifiers);

                        AbstractBeanDefinition jmsMockBeanDefinition = new RootBeanDefinition(JmsMock.class);
                        ConstructorArgumentValues jmsMockBeanDefinitionCav = new ConstructorArgumentValues();
                        jmsMockBeanDefinitionCav.addIndexedArgumentValue(0, new RuntimeBeanReference(meta.name()));
                        jmsMockBeanDefinitionCav.addIndexedArgumentValue(1, new RuntimeBeanReference(destinationManagerBeanName));
                        jmsMockBeanDefinition.setConstructorArgumentValues(jmsMockBeanDefinitionCav);
                        extendedBeanDefinitionRegistry.registerBeanDefinition(jmsMockBeanName, jmsMockBeanDefinition, qualifiers);
                    });
                    extendedBeanDefinitionRegistry.registerBeanDefinition(JmsMockLocator.class);
                    extendedBeanDefinitionRegistry.registerBeanDefinition(DestinationManagerResetter.class);
                    if (Classes.isPresent("org.springframework.jms.config.AbstractJmsListenerContainerFactory")) {
                        extendedBeanDefinitionRegistry.registerBeanDefinition(JmsListenerContainerFactoryConfigurer.class);
                    }
                } else {
                    throw new IllegalStateException("\nAutomocker is missing class [" + MOCKRUNNER_CONNECTION_FACTORY_CLASS + "] to mock " + connectionFactoryBeans.size() + " bean(s) of type [" + DataSource.class.getName() + "]: " +
                        connectionFactoryBeans.stream().map(ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata::name).collect(Collectors.joining(", ")) +
                        "\nMake sure mockrunner-jms.jar is in the test classpath");
                }
            }
        }
    }
}
