package com.github.fridujo.sample.seller;

import com.github.fridujo.sample.Broker;
import com.github.fridujo.sample.BusinessHeaders;
import com.github.fridujo.sample.JmsListenerContainerFactories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public class Seller {
    private final Logger logger = LoggerFactory.getLogger("seller");

    private final JmsTemplate sellerSender;
    private final JmsTemplate buyerSender;
    private final String id;
    private final Set<Consumer<String>> onItemSoldListeners = new LinkedHashSet<>();

    Seller(@Broker(Broker.Type.SELLER) JmsTemplate sellerSender, @Broker(Broker.Type.BUYER) JmsTemplate buyerSender) {
        this.sellerSender = sellerSender;
        this.buyerSender = buyerSender;
        this.id = UUID.randomUUID().toString();
    }

    public void addItemSoldListener(Consumer<String> onItemSoldListener) {
        onItemSoldListeners.add(onItemSoldListener);
    }

    @JmsListener(destination = "REQUESTED_ITEM", containerFactory = JmsListenerContainerFactories.SELLER)
    void onRequestedItem(@Payload String item,
                         @Header(BusinessHeaders.BUYER_ID) String buyerId) {
        if (item.startsWith("available")) {
            sellItem(item, buyerId);
            logger.info("Seller " + id + " sold item [" + item + "] to buyer " + buyerId);
        } else {
            logger.info("Seller " + id + " cannot sold unavailable item [" + item + "] to buyer " + buyerId);
        }
    }

    @JmsListener(destination = "SOLD", containerFactory = JmsListenerContainerFactories.SELLER)
    void onSoldItem(@Payload String item,
                    @Header(BusinessHeaders.BUYER_ID) String buyerId,
                    @Header(BusinessHeaders.SELLER_ID) String sellerId) {
        buyerSender.send("BOUGHT", session -> {
            TextMessage textMessage = session.createTextMessage(item);
            textMessage.setStringProperty(BusinessHeaders.BUYER_ID, buyerId);
            textMessage.setStringProperty(BusinessHeaders.SELLER_ID, sellerId);
            return textMessage;
        });
        logger.info("Seller " + id + " sent item [" + item + "] to buyer " + buyerId);
        onItemSoldListeners.forEach(onItemSoldListener -> onItemSoldListener.accept(item));
    }

    private void sellItem(String item, String buyerId) {
        sellerSender.send("SOLD", session -> {
            TextMessage textMessage = session.createTextMessage(item);
            textMessage.setStringProperty(BusinessHeaders.BUYER_ID, buyerId);
            textMessage.setStringProperty(BusinessHeaders.SELLER_ID, id);
            return textMessage;
        });
    }
}
