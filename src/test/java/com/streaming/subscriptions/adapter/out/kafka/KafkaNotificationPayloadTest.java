package com.streaming.subscriptions.adapter.out.kafka;

import com.streaming.subscriptions.domain.exception.InvalidNotificationPayloadException;
import com.streaming.subscriptions.domain.model.Notification;
import com.streaming.subscriptions.domain.model.NotificationType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KafkaNotificationPayloadTest {

    @Test
    void toDomain_whenValidPayload_shouldReturnNotification() {
        var payload = new KafkaNotificationPayload(1L, "SUBSCRIPTION_PURCHASED", "2025-02-16T12:00:00Z");

        Notification result = payload.toDomain();

        assertThat(result.getSubscriptionId()).isEqualTo(1L);
        assertThat(result.getType()).isEqualTo(NotificationType.SUBSCRIPTION_PURCHASED);
        assertThat(result.getOccurredAt()).isNotNull();
    }

    @Test
    void toDomain_whenSubscriptionIdNull_shouldThrowInvalidNotificationPayloadException() {
        var payload = new KafkaNotificationPayload(null, "SUBSCRIPTION_PURCHASED", "2025-02-16T12:00:00Z");

        assertThatThrownBy(payload::toDomain)
                .isInstanceOf(InvalidNotificationPayloadException.class)
                .hasMessageContaining("subscriptionId is required");
    }

    @Test
    void toDomain_whenTypeNull_shouldThrowInvalidNotificationPayloadException() {
        var payload = new KafkaNotificationPayload(1L, null, "2025-02-16T12:00:00Z");

        assertThatThrownBy(payload::toDomain)
                .isInstanceOf(InvalidNotificationPayloadException.class)
                .hasMessageContaining("type is required");
    }

    @Test
    void toDomain_whenTypeBlank_shouldThrowInvalidNotificationPayloadException() {
        var payload = new KafkaNotificationPayload(1L, "   ", "2025-02-16T12:00:00Z");

        assertThatThrownBy(payload::toDomain)
                .isInstanceOf(InvalidNotificationPayloadException.class)
                .hasMessageContaining("type is required");
    }

    @Test
    void toDomain_whenInvalidType_shouldThrowInvalidNotificationPayloadException() {
        var payload = new KafkaNotificationPayload(1L, "INVALID_TYPE", "2025-02-16T12:00:00Z");

        assertThatThrownBy(payload::toDomain)
                .isInstanceOf(InvalidNotificationPayloadException.class)
                .hasMessageContaining("Invalid notification type")
                .hasMessageContaining("INVALID_TYPE");
    }

    @Test
    void toDomain_whenOccurredAtNull_shouldUseCurrentTime() {
        var payload = new KafkaNotificationPayload(1L, "SUBSCRIPTION_CANCELED", null);

        Notification result = payload.toDomain();

        assertThat(result.getSubscriptionId()).isEqualTo(1L);
        assertThat(result.getType()).isEqualTo(NotificationType.SUBSCRIPTION_CANCELED);
        assertThat(result.getOccurredAt()).isNotNull();
    }

    @Test
    void toDomain_whenOccurredAtBlank_shouldUseCurrentTime() {
        var payload = new KafkaNotificationPayload(1L, "SUBSCRIPTION_CANCELED", "");

        Notification result = payload.toDomain();

        assertThat(result.getSubscriptionId()).isEqualTo(1L);
        assertThat(result.getType()).isEqualTo(NotificationType.SUBSCRIPTION_CANCELED);
        assertThat(result.getOccurredAt()).isNotNull();
    }

    @Test
    void toDomain_whenSubscriptionCanceled_shouldParseCorrectly() {
        var payload = new KafkaNotificationPayload(2L, "SUBSCRIPTION_CANCELED", "2025-02-16T14:30:00Z");

        Notification result = payload.toDomain();

        assertThat(result.getSubscriptionId()).isEqualTo(2L);
        assertThat(result.getType()).isEqualTo(NotificationType.SUBSCRIPTION_CANCELED);
        assertThat(result.getOccurredAt().toString()).contains("2025-02-16");
    }

    @Test
    void toDomain_whenTypeHasWhitespace_shouldTrimAndParse() {
        var payload = new KafkaNotificationPayload(1L, "  SUBSCRIPTION_PURCHASED  ", "2025-02-16T12:00:00Z");

        Notification result = payload.toDomain();

        assertThat(result.getType()).isEqualTo(NotificationType.SUBSCRIPTION_PURCHASED);
    }
}
