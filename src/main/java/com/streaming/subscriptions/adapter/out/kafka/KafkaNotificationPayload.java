package com.streaming.subscriptions.adapter.out.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.streaming.subscriptions.domain.model.Notification;
import com.streaming.subscriptions.domain.model.NotificationType;

import java.time.Instant;

/**
 * JSON shape produced by {@link KafkaMessagePublisherAdapter} and consumed from Kafka.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KafkaNotificationPayload(Long subscriptionId, String type, String occurredAt) {

    public Notification toDomain() {
        if (subscriptionId == null) {
            throw new IllegalArgumentException("subscriptionId is required");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("type is required");
        }
        NotificationType notificationType = NotificationType.valueOf(type.trim());
        Instant at = (occurredAt != null && !occurredAt.isBlank())
                ? Instant.parse(occurredAt)
                : Instant.now();
        return new Notification(subscriptionId, notificationType, at);
    }
}
