package com.github.fridujo.sample.jms.utils;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;

import javax.jms.ConnectionFactory;

public class ActiveMQConnectionFactoryFactory {

    private final ActiveMQProperties activeMQProperties;

    public ActiveMQConnectionFactoryFactory(ActiveMQProperties activeMQProperties) {
        this.activeMQProperties = activeMQProperties;
    }

    public ConnectionFactory create() {
        String username = activeMQProperties.getUser();
        String password = activeMQProperties.getPassword();
        String brokerUrl = activeMQProperties.getBrokerUrl();

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(username, password, brokerUrl);
        return connectionFactory;
    }
}
