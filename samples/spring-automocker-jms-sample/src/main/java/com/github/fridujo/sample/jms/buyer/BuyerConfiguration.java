package com.github.fridujo.sample.jms.buyer;

import com.github.fridujo.sample.jms.Broker;
import com.github.fridujo.sample.jms.JmsListenerContainerFactories;
import com.github.fridujo.sample.jms.utils.ActiveMQConnectionFactoryFactory;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;

@Configuration
class BuyerConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "buyer.activemq")
    @Broker(Broker.Type.BUYER)
    ActiveMQProperties buyerActiveMQProperties() {
        return new ActiveMQProperties();
    }

    @Bean
    @Broker(Broker.Type.BUYER)
    ConnectionFactory buyerConnectionFactory(@Broker(Broker.Type.BUYER) ActiveMQProperties activeMQProperties) {
        return new ActiveMQConnectionFactoryFactory(activeMQProperties).create();
    }

    @Bean(name = JmsListenerContainerFactories.BUYER)
    @Broker(Broker.Type.BUYER)
    JmsListenerContainerFactory<DefaultMessageListenerContainer> buyerJmsListenerContainerFactory(@Broker(Broker.Type.BUYER) ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    @Broker(Broker.Type.BUYER)
    JmsTemplate buyerJmsTemplate(@Broker(Broker.Type.BUYER) ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }
}
