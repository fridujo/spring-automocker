package com.github.fridujo.automocker.api.jms;

import com.mockrunner.mock.jms.MockDestination;
import org.assertj.core.api.Assertions;

import javax.jms.Message;
import java.util.List;

public class JmsDestinationAssert {

    private final String destinationName;
    private final MockDestination destination;

    public JmsDestinationAssert(MockDestination mockDestination, String destinationName) {
        this.destination = mockDestination;
        this.destinationName = destinationName;
    }

    public JmsDestinationAssert hasSize(int expected) {
        Assertions.assertThat(destination.getReceivedMessageList()
            .size())
            .isEqualTo(expected);
        return this;
    }

    public JmsMessageAssert consumingFirstMessage() {
        if (destination.isEmpty()) {
            throw new IllegalArgumentException(
                "No message available on destination [" + destinationName + "]");
        }
        return new JmsMessageAssert(destination.getMessage());
    }

    @SuppressWarnings("unchecked")
    public List<Message> getMessages() {
        return destination.getCurrentMessageList();
    }

    public String toString() {
        return "Asserter on JMS destination [" + destinationName + "]";
    }
}
