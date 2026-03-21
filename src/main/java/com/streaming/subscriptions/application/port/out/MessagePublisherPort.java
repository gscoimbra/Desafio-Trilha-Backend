package com.streaming.subscriptions.application.port.out;

import com.streaming.subscriptions.domain.model.Notification;

/**
 * Publishes a notification to the messaging layer (e.g. Kafka).
 */
public interface MessagePublisherPort {

    void publish(Notification notification);
}
