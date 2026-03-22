package com.streaming.subscriptions.adapter.in.rest.dto;

import com.streaming.subscriptions.domain.exception.InvalidNotificationTypeException;
import com.streaming.subscriptions.domain.model.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Request to subscribe or unsubscribe a user")
public class NotificationRequestDto {

    @NotNull(message = "userId is required")
    @Schema(description = "ID of the user whose subscription to update", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotBlank(message = "type is required")
    @Schema(
            description = "SUBSCRIPTION_PURCHASED → ativa, SUBSCRIPTION_CANCELED → cancelada",
            example = "SUBSCRIPTION_PURCHASED",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"SUBSCRIPTION_PURCHASED", "SUBSCRIPTION_CANCELED"}
    )
    private String type;

    public NotificationType getNotificationType() {
        try {
            return NotificationType.valueOf(type.trim());
        } catch (IllegalArgumentException e) {
            throw new InvalidNotificationTypeException(type);
        }
    }

    public com.streaming.subscriptions.domain.model.Notification toDomain(Long subscriptionId) {
        return new com.streaming.subscriptions.domain.model.Notification(
                subscriptionId, getNotificationType(), Instant.now());
    }
}
