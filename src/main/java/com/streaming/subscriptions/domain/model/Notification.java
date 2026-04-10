package com.streaming.subscriptions.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Representação de domínio de uma notificação, evento de assinatura.
 * Por ser final, uma vez criada, não será alterada.
 */
public final class Notification {

    // Qual assinatura será afetada
    private final Long subscriptionId;

    // O tipo do evento (SUBSCRIPTION_PURCHASED ou SUBSCRIPTION_CANCELED)
    private final NotificationType type;

    // Quando o evento ocorreu(timestamp), usado para auditoria(event_history)
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

/**
 * Caso de uso:
 * O REST cria uma Notification e manda para o ReceiveNotificationUseCase, que publica no Kafka.
 * O Consumer transforma o JSON em Notification e manda para o ProcessNotificationUseCase, que atualiza a assinatura e salva no histórico.
 */