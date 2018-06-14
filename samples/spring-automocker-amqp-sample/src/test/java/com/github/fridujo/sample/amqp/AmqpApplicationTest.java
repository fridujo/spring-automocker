package com.github.fridujo.sample.amqp;

import com.github.fridujo.automocker.api.metrics.GraphiteMock;
import com.github.fridujo.automocker.base.Automocker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@Automocker
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AmqpApplication.class)
class AmqpApplicationTest {

    private final Sender sender;
    private final Receiver receiver;
    private final GraphiteMock graphiteMock;

    AmqpApplicationTest(
        @Autowired Sender sender,
        @Autowired Receiver receiver,
        @Autowired GraphiteMock graphiteMock) {
        this.sender = sender;
        this.receiver = receiver;
        this.graphiteMock = graphiteMock;
    }

    @Test
    void message_is_received_when_sent_by_sender() {
        sender.send();
        List<String> receivedMessages = new ArrayList<>();
        assertTimeoutPreemptively(ofMillis(500L), () -> {
                while (receivedMessages.isEmpty()) {
                    receivedMessages.addAll(receiver.getMessages());
                    TimeUnit.MILLISECONDS.sleep(100L);
                }
            }
        );

        assertThat(receivedMessages).containsExactly("Hello from RabbitMQ!");


        graphiteMock.afterReporting()
            .assertThatMetric("rabbitmqPublished.name.rabbit.count").hasLastValue(1);
        graphiteMock.assertThatMetric("rabbitmqConsumed.name.rabbit.count").hasLastValue(1);
    }
}
