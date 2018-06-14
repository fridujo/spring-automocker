package com.github.fridujo.sample.jms;

import com.github.fridujo.automocker.api.jms.JmsMock;
import com.github.fridujo.automocker.base.Automocker;
import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Automocker
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JmsApplication.class)
class JmsApplicationTest {

    @Autowired
    @Broker(Broker.Type.BUYER)
    private JmsMock buyerJmsMock;

    @Test
    void buyer_receives_item_when_available() {
        String item = "available AC/DC concert tickets";
        buyerJmsMock.sendText("REQUEST_ITEM", item);

        buyerJmsMock.assertThatDestination("BOUGHT")
            .consumingFirstMessage()
            .textSatisfies(actual -> actual.isEqualTo(item))
            .headerSatisfies(BusinessHeaders.BUYER_ID, AbstractAssert::isNotNull)
            .headerSatisfies(BusinessHeaders.SELLER_ID, AbstractAssert::isNotNull);
    }
}
