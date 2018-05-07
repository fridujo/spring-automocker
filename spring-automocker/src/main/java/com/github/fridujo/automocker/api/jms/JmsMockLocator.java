package com.github.fridujo.automocker.api.jms;

import javax.jms.ConnectionFactory;
import java.util.Optional;
import java.util.Set;

/**
 * Used to find {@link JmsMock} by {@link ConnectionFactory}.
 *
 * @see JmsListenerContainerFactoryConfigurer
 */
public class JmsMockLocator {

    private final Set<JmsMock> jmsMocks;

    JmsMockLocator(Set<JmsMock> jmsMocks) {
        this.jmsMocks = jmsMocks;
    }

    Optional<JmsMock> getJmsMockByConnectionFactory(ConnectionFactory connectionFactory) {
        return jmsMocks
            .stream()
            .filter(jmsMock -> jmsMock.connectionFactory == connectionFactory)
            .findFirst();
    }
}
