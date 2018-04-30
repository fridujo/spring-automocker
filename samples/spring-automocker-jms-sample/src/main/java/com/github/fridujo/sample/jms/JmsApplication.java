package com.github.fridujo.sample.jms;

import com.github.fridujo.sample.jms.seller.Seller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.TextMessage;
import java.util.concurrent.Semaphore;

@SpringBootApplication(exclude = {JmsAutoConfiguration.class})
@EnableJms
public class JmsApplication {

    public static void main(String[] args) throws InterruptedException {
        try (ConfigurableApplicationContext applicationContext = SpringApplication.run(JmsApplication.class)) {
            Semaphore waitForSellerToSendItem = new Semaphore(0);
            Seller buyer = applicationContext.getBean(Seller.class);
            buyer.addItemSoldListener(item -> waitForSellerToSendItem.release());
            JmsTemplate buyerJmsTemplate = applicationContext.getBean("buyerJmsTemplate", JmsTemplate.class);
            buyerJmsTemplate.send("REQUEST_ITEM", session -> {
                TextMessage textMessage = session.createTextMessage("available concert tickets");
                return textMessage;
            });
            waitForSellerToSendItem.acquire();
        }
    }
}
