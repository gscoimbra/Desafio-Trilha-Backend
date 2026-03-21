package com.streaming.subscriptions.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.streaming.subscriptions.domain.model.Notification;
import com.streaming.subscriptions.domain.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {

    @NotNull(message = "subscriptionId is required")
    private Long subscriptionId;

    @NotBlank(message = "type is required")
    private String type;

    /**
     * When the event occurred; defaults to "now" if omitted. Accepts JSON field {@code timestamp} as alias.
     */
    @JsonAlias("timestamp")
    private Instant occurredAt;

    public Notification toDomain() {
        NotificationType notificationType;
        try {
            notificationType = NotificationType.valueOf(type.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid notification type: " + type + ". Expected one of: SUBSCRIPTION_PURCHASED, SUBSCRIPTION_CANCELED, SUBSCRIPTION_RESTARTED.");
        }
        Instant at = occurredAt != null ? occurredAt : Instant.now();
        return new Notification(subscriptionId, notificationType, at);
    }
}
