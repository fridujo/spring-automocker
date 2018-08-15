package com.github.fridujo.automocker.api.kafka;

import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.kafka.core.ProducerFactory;

public class MockProducerFactory<K, V> implements ProducerFactory<K, V> {
    @Override
    public Producer<K, V> createProducer() {
        return new MockProducer<>();
    }
}
