package com.github.fridujo.sample.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaSender {

    private final Logger logger = LoggerFactory.getLogger(KafkaSender.class);
    private final KafkaTemplate kafkaTemplate;

    public KafkaSender(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void sendMessage() {
        String message = UUID.randomUUID().toString();
        kafkaTemplate.send(KafkaApplication.APP_TOPIC, message);
        logger.info("Sent message: " + message);
    }
}
