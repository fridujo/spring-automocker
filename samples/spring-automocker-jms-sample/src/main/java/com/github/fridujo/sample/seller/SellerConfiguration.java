package com.github.fridujo.sample.seller;

import com.github.fridujo.sample.Broker;
import com.github.fridujo.sample.JmsListenerContainerFactories;
import com.github.fridujo.sample.utils.ActiveMQConnectionFactoryFactory;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
class SellerConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "seller.activemq")
    @Broker(Broker.Type.SELLER)
    ActiveMQProperties sellerActiveMQProperties() {
        return new ActiveMQProperties();
    }

    @Bean
    @Broker(Broker.Type.SELLER)
    ConnectionFactory sellerConnectionFactory(@Broker(Broker.Type.SELLER) ActiveMQProperties activeMQProperties) {
        return new ActiveMQConnectionFactoryFactory(activeMQProperties).create();
    }

    @Bean(name = JmsListenerContainerFactories.SELLER)
    @Broker(Broker.Type.SELLER)
    DefaultJmsListenerContainerFactory sellerJmsListenerContainerFactory(@Broker(Broker.Type.SELLER) ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    @Broker(Broker.Type.SELLER)
    JmsTemplate sellerJmsTemplate(@Broker(Broker.Type.SELLER) ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }
}
