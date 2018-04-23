package com.github.fridujo.automocker.api.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import static org.assertj.core.api.Assertions.assertThat;

public class JmsMessageAssert {

    private Message message;

    JmsMessageAssert(Message message) {
        this.message = message;
    }

    public JmsMessageAssert hasText(String messagePayload) {
        assertThat(message).isInstanceOf(TextMessage.class);
        try {
            assertThat(((TextMessage) message).getText()).isEqualTo(messagePayload);
        } catch (JMSException e) {
            throw new IllegalStateException("Unable to retrieve 'text' from a " + TextMessage.class.getName());
        }
        return this;
    }

    public JmsMessageAssert hasHeader(String name) {
        try {
            assertThat(message.getObjectProperty(name)).as("JMS property [" + name + "]").isNotNull();
        } catch (JMSException e) {
            throw new IllegalStateException("Unable to read JMS property [" + name + "]");
        }
        return this;
    }

    public JmsMessageAssert hasHeader(String name, Object value) {
        try {
            assertThat(message.getObjectProperty(name)).as("JMS property [" + name + "]").isEqualTo(value);
        } catch (JMSException e) {
            throw new IllegalStateException("Unable to read JMS property [" + name + "]");
        }
        return this;
    }
}
