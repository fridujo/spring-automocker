package com.github.fridujo.automocker.api.jms;

import com.github.fridujo.automocker.utils.ThrowingFunction;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.ObjectAssert;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class JmsMessageAssert {

    private Message message;

    JmsMessageAssert(Message message) {
        this.message = message;
    }

    public ObjectAssert<Object> header(String name) {
        try {
            return assertThat(message.getObjectProperty(name)).as("JMS property [" + name + "]");
        } catch (JMSException e) {
            throw new IllegalStateException("Unable to read JMS property [" + name + "]", e);
        }
    }

    public JmsMessageAssert headerSatisfies(String name, Consumer<ObjectAssert<Object>> matcherUse) {
        matcherUse.accept(header(name));
        return this;
    }

    public Map<String, Object> getHeaders() {
        try {
            return (Map<String, Object>) Collections.list(message.getPropertyNames())
                .stream()
                .collect(Collectors.toMap(Function.identity(), ThrowingFunction.silent(propertyName -> message.getObjectProperty((String) propertyName))));
        } catch (JMSException e) {
            throw new IllegalStateException("Unable to read JMS properties", e);
        }
    }

    public AbstractCharSequenceAssert<?, String> text() {
        assertThat(message).isInstanceOf(TextMessage.class);
        try {
            return assertThat(((TextMessage) message).getText());
        } catch (JMSException e) {
            throw new IllegalStateException("Unable to retrieve 'text' from a " + TextMessage.class.getName(), e);
        }
    }

    public JmsMessageAssert textSatisfies(Consumer<AbstractCharSequenceAssert<?, String>> matcherUse) {
        matcherUse.accept(text());
        return this;
    }

    public String getText() {
        assertThat(message).isInstanceOf(TextMessage.class);
        try {
            return ((TextMessage) message).getText();
        } catch (JMSException e) {
            throw new IllegalStateException("Unable to retrieve 'text' from a " + TextMessage.class.getName(), e);
        }
    }
}
