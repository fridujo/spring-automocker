package com.github.fridujo.automocker.api.jms;

import com.github.fridujo.automocker.api.Resettable;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockDestination;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.Set;

public class DestinationManagerResetter implements Resettable {

    private final Set<DestinationManager> destinationManagers;

    public DestinationManagerResetter(Set<DestinationManager> destinationManagers) {
        this.destinationManagers = destinationManagers;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reset() {
        for (DestinationManager destinationManager : destinationManagers) {
            // TODO make a PR to mockrunner-jms to avoid these reflective accesses
            Map<String, MockDestination> queues =
                (Map<String, MockDestination>) ReflectionTestUtils.getField(destinationManager, "queues");
            Map<String, MockDestination> topics =
                (Map<String, MockDestination>) ReflectionTestUtils.getField(destinationManager, "topics");
            queues.values().forEach(d -> d.reset());
            topics.values().forEach(d -> d.reset());
        }
    }
}
