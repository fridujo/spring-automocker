package com.github.fridujo.automocker.api.jms;

import com.github.fridujo.automocker.utils.Maps;
import com.github.fridujo.automocker.utils.ThrowingBiConsumer;
import com.github.fridujo.automocker.utils.ThrowingConsumer;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class JmsMessageBuilder {

    private String messageId;
    private Long timestamp;
    private String correlationId;
    private String replyTo;
    private Integer deliveryMode;
    private Boolean redelivered;
    private String type;
    private Long expiration;
    private Integer priority;

    private Map<String, Object> properties = new HashMap<>();

    private JmsMessageBuilder() {
    }

    public static TextMessageBuilder newTextMessage(String text) {
        return new TextMessageBuilder(text);
    }

    public static ObjectMessageBuilder newObjectMessage(Serializable object) {
        return new ObjectMessageBuilder(object);
    }

    public static MapMessageBuilder newMapMessage(Map<String, ?> objects) {
        return new MapMessageBuilder().setObjects(objects);
    }

    public static MapMessageBuilder newMapMessage(Object... objects) {
        return new MapMessageBuilder().setObjects(objects);
    }

    public JmsMessageBuilder addProperty(String name, Object value) {
        this.properties.put(name, value);
        return this;
    }

    public JmsMessageBuilder addProperties(Object... properties) {
        Maps.build(String.class, properties)
            .forEach((k, v) -> this.properties.put(k, v));
        return this;
    }

    public JmsMessageBuilder setJMSMessageID(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public JmsMessageBuilder setJMSTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public JmsMessageBuilder setJMSCorrelationID(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public JmsMessageBuilder setJMSReplyTo(String replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public JmsMessageBuilder setJMSDeliveryMode(int deliveryMode) {
        this.deliveryMode = deliveryMode;
        return this;
    }

    public JmsMessageBuilder setJMSRedelivered(boolean redelivered) {
        this.redelivered = redelivered;
        return this;
    }

    public JmsMessageBuilder setJMSType(String type) {
        this.type = type;
        return this;
    }

    public JmsMessageBuilder setJMSExpiration(long expiration) {
        this.expiration = expiration;
        return this;
    }

    public JmsMessageBuilder setJMSPriority(int priority) {
        this.priority = priority;
        return this;
    }

    protected abstract Message toMessageInternal(Session session) throws JMSException;

    public Message toMessage(Session session) throws JMSException {
        Message message = toMessageInternal(session);
        if (messageId != null)
            message.setJMSMessageID(messageId);
        if (timestamp != null)
            message.setJMSTimestamp(timestamp);
        if (correlationId != null)
            message.setJMSCorrelationID(correlationId);
        if (replyTo != null)
            message.setJMSReplyTo(session.createQueue(replyTo));
        if (deliveryMode != null)
            message.setJMSDeliveryMode(deliveryMode);
        if (redelivered != null)
            message.setJMSRedelivered(redelivered);
        if (type != null)
            message.setJMSType(type);
        if (expiration != null)
            message.setJMSExpiration(expiration);
        if (priority != null)
            message.setJMSPriority(priority);

        properties.entrySet()
            .forEach(ThrowingConsumer.silent(e -> message.setObjectProperty(e.getKey(), e.getValue())));
        return message;
    }

    public static final class TextMessageBuilder extends JmsMessageBuilder {
        private final String text;

        private TextMessageBuilder(String text) {
            this.text = text;
        }

        @Override
        public Message toMessageInternal(Session session) throws JMSException {
            return session.createTextMessage(text);
        }
    }

    public static final class ObjectMessageBuilder extends JmsMessageBuilder {
        private final Serializable object;

        private ObjectMessageBuilder(Serializable object) {
            this.object = object;
        }

        @Override
        public Message toMessageInternal(Session session) throws JMSException {
            return session.createObjectMessage(object);
        }
    }

    public static final class MapMessageBuilder extends JmsMessageBuilder {
        private final Map<String, Object> map = new HashMap<>();

        private MapMessageBuilder() {
        }

        public MapMessageBuilder setObject(String name, Object value) {
            this.map.put(name, value);
            return this;
        }

        public MapMessageBuilder setObjects(Object... namesAndValues) {
            setObjects(Maps.build(String.class, namesAndValues));
            return this;
        }

        public MapMessageBuilder setObjects(Map<String, ?> map) {
            Optional.ofNullable(map)
                .ifPresent(m -> m.forEach((k, v) -> this.map.put(k, v)));
            return this;
        }

        @Override
        public Message toMessageInternal(Session session) throws JMSException {
            MapMessage message = session.createMapMessage();
            map.forEach(ThrowingBiConsumer.silent((k, v) -> message.setObject(k, v)));
            return message;
        }
    }

}
