package com.streaming.subscriptions.adapter.in.rest.dto;

import com.streaming.subscriptions.domain.exception.InvalidNotificationTypeException;
import com.streaming.subscriptions.domain.model.Notification;
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
@Schema(description = "Subscription notification payload")
public class NotificationRequestDto {

    @NotNull(message = "subscriptionId is required")
    @Schema(description = "ID of the subscription to update", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long subscriptionId;

    @NotBlank(message = "type is required")
    @Schema(
            description = "Notification type. SUBSCRIPTION_PURCHASED → ativa, SUBSCRIPTION_CANCELED → cancelada",
            example = "SUBSCRIPTION_PURCHASED",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"SUBSCRIPTION_PURCHASED", "SUBSCRIPTION_CANCELED"}
    )
    private String type;

    public Notification toDomain() {
        NotificationType notificationType;
        try {
            notificationType = NotificationType.valueOf(type.trim());
        } catch (IllegalArgumentException e) {
            throw new InvalidNotificationTypeException(type);
        }
        return new Notification(subscriptionId, notificationType, Instant.now());
    }
}
