package com.streaming.subscriptions.adapter.out.kafka;

import com.streaming.subscriptions.application.port.out.MessagePublisherPort;
import com.streaming.subscriptions.domain.model.Notification;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Test profile implementation when Kafka auto-configuration is disabled.
 */
@Component
@Profile("test")
public class NoOpMessagePublisherAdapter implements MessagePublisherPort {

    @Override
    public void publish(Notification notification) {
        // no-op: integration tests can replace with mocks if needed
    }
}
