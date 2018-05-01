package com.github.fridujo.automocker.api.jms;

import org.junit.jupiter.api.Test;

import javax.jms.ConnectionFactory;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class JmsMockLocatorTest {

    @Test
    void getJmsMockByConnectionFactory_returns_matching_object() {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        ConnectionFactory connectionFactory2 = mock(ConnectionFactory.class);
        ConnectionFactory connectionFactory3 = mock(ConnectionFactory.class);
        JmsMock jmsMock = new JmsMock(connectionFactory, null);
        JmsMock jmsMock2 = new JmsMock(connectionFactory2, null);

        assertThat(connectionFactory == connectionFactory2).isFalse();
        assertThat(jmsMock == jmsMock2).isFalse();

        Set<JmsMock> jmsMocks = new HashSet<>();
        jmsMocks.add(jmsMock);
        jmsMocks.add(jmsMock2);

        JmsMockLocator jmsMockLocator = new JmsMockLocator(jmsMocks);

        assertThat(jmsMockLocator.getJmsMockByConnectionFactory(connectionFactory)).contains(jmsMock);
        assertThat(jmsMockLocator.getJmsMockByConnectionFactory(connectionFactory2)).contains(jmsMock2);
        assertThat(jmsMockLocator.getJmsMockByConnectionFactory(connectionFactory3)).isEmpty();
    }
}
