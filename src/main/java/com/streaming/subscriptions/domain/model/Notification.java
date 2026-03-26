package com.streaming.subscriptions.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Representação de domínio de uma notificação de assinatura (entrada via API / Kafka).
 */
public final class Notification {

    private final Long subscriptionId;
    private final NotificationType type;
    private final Instant occurredAt;

    public Notification(Long subscriptionId, NotificationType type, Instant occurredAt) {
        this.subscriptionId = Objects.requireNonNull(subscriptionId, "subscriptionId");
        this.type = Objects.requireNonNull(type, "type");
        this.occurredAt = occurredAt != null ? occurredAt : Instant.now();
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public NotificationType getType() {
        return type;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
