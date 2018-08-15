package com.github.fridujo.sample.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaReceiver {

    private final Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);

    @KafkaListener(topics = KafkaApplication.APP_TOPIC, groupId = "test")
    public void processMessage(@Payload String content,
                               @Headers Map<String, Object> headers) {
        logger.info("Received message: " + content + "(" + headers + ")");
    }
}
