package com.streaming.subscriptions.adapter.out.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.streaming.subscriptions.domain.exception.InvalidNotificationPayloadException;
import com.streaming.subscriptions.domain.model.Notification;
import com.streaming.subscriptions.domain.model.NotificationType;

import java.time.Instant;

/**
 * Formato JSON produzido por {@link KafkaMessagePublisherAdapter} e consumido do Kafka.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KafkaNotificationPayload(Long subscriptionId, String type, String occurredAt) {

    public Notification toDomain() {
        if (subscriptionId == null) {
            throw new InvalidNotificationPayloadException("subscriptionId is required");
        }
        if (type == null || type.isBlank()) {
            throw new InvalidNotificationPayloadException("type is required");
        }
        try {
            NotificationType notificationType = NotificationType.valueOf(type.trim());
            Instant at = (occurredAt != null && !occurredAt.isBlank())
                    ? Instant.parse(occurredAt)
                    : Instant.now();
            return new Notification(subscriptionId, notificationType, at);
        } catch (IllegalArgumentException error) {
            throw new InvalidNotificationPayloadException("Invalid notification type in payload: " + type, error);
        }
    }
}
