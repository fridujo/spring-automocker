package com.github.fridujo.automocker.api.jms;

import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockDestination;
import org.springframework.jms.config.AbstractJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ErrorHandler;

import javax.jms.ConnectionFactory;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class JmsMock {

    final ConnectionFactory connectionFactory;
    private final JmsTemplate jmsTemplate;
    private final DestinationManager destinationManager;

    private Optional<ErrorHandlerMock> errorHandlerMock = Optional.empty();

    public JmsMock(ConnectionFactory connectionFactory, DestinationManager destinationManager) {
        this.connectionFactory = connectionFactory;
        this.jmsTemplate = new JmsTemplate(connectionFactory);
        this.destinationManager = destinationManager;
    }

    public void sendText(String destinationName, String textPayload) {
        sendText(destinationName, JmsMessageBuilder.newTextMessage(textPayload));
    }

    public void sendText(String destinationName, String textPayload, Object... properties) {
        sendText(destinationName, JmsMessageBuilder.newTextMessage(textPayload)
            .addProperties(properties));
    }

    public void sendText(String destinationName, JmsMessageBuilder messageBuilder) {
        jmsTemplate.send(destinationName, messageBuilder::toMessage);
    }

    /**
     * May wait until 1500 ms for the destination to exists (be dynamically created).
     *
     * @throws IllegalArgumentException if the destination can not be found after 1500 ms
     * @throws IllegalStateException    if interrupted while waiting for destination to exists
     */
    public JmsDestinationAssert assertThatDestination(String destinationName) throws IllegalArgumentException, IllegalStateException {
        Optional<MockDestination> potentialDestination = tryGettingDestination(destinationName, 3, 500L);
        if (potentialDestination.isPresent()) {
            return new JmsDestinationAssert(potentialDestination.get(), destinationName);
        } else {
            throw new IllegalArgumentException("Destination [" + destinationName + "] does not exists");
        }

    }

    private Optional<MockDestination> tryGettingDestination(String destinationName, int tryNumber, long durationInMsBetweenTries) throws IllegalStateException {
        Optional<MockDestination> potentialDestination = Optional.empty();
        for (int tryIndex = 0; tryIndex < tryNumber; tryIndex++) {
            potentialDestination = immediatlyGetDestination(destinationManager, destinationName);
            if (potentialDestination.isPresent()) {
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(durationInMsBetweenTries);
            } catch (InterruptedException e) {
                throw new IllegalStateException("Interrupted while waiting for destination [" + destinationName + "] to exists");
            }
        }
        return potentialDestination;
    }

    private Optional<MockDestination> immediatlyGetDestination(DestinationManager destinationManager, String destinationName) {
        final Optional<MockDestination> destination;
        if (destinationManager.existsQueue(destinationName)) {
            destination = Optional.of(destinationManager.getQueue(destinationName));
        } else if (destinationManager.existsTopic(destinationName)) {
            destination = Optional.of(destinationManager.getTopic(destinationName));
        } else {
            destination = Optional.empty();
        }
        return destination;
    }

    public ErrorHandlerMock containerErrorHandler() {
        if (!errorHandlerMock.isPresent()) {
            throw new IllegalStateException("No " + ErrorHandler.class.getSimpleName()
                + " available, make sure @MockJms is present and an "
                + AbstractJmsListenerContainerFactory.class.getSimpleName()
                + " is configured in your Spring configuration");
        }
        return errorHandlerMock.get();
    }

    void setErrorHandlerMock(ErrorHandlerMock errorHandlerMock) {
        this.errorHandlerMock = Optional.of(errorHandlerMock);
    }
}
