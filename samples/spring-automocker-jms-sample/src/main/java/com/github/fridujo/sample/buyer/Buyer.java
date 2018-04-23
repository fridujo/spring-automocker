package com.github.fridujo.sample.buyer;

import com.github.fridujo.sample.Broker;
import com.github.fridujo.sample.BusinessHeaders;
import com.github.fridujo.sample.JmsListenerContainerFactories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;
import java.util.UUID;

@Component
class Buyer {
    private final Logger logger = LoggerFactory.getLogger("buyer");
    private final JmsTemplate sellerSender;
    private final String id;

    Buyer(@Broker(Broker.Type.SELLER) JmsTemplate sellerSender) {
        this.sellerSender = sellerSender;
        this.id = UUID.randomUUID().toString();
    }

    @JmsListener(destination = "REQUEST_ITEM", containerFactory = JmsListenerContainerFactories.BUYER)
    void onItemRequest(@Payload String item) {
        sellerSender.send("REQUESTED_ITEM", session -> {
            TextMessage textMessage = session.createTextMessage(item);
            textMessage.setStringProperty(BusinessHeaders.BUYER_ID, id);
            return textMessage;
        });
        logger.info("Buyer " + id + " sent request for [" + item + "]");
    }
}
