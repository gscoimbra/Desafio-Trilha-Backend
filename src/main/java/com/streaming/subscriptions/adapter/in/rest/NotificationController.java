package com.streaming.subscriptions.adapter.in.rest;

import com.streaming.subscriptions.adapter.in.rest.dto.NotificationRequestDto;
import com.streaming.subscriptions.adapter.in.rest.error.ApiError;
import com.streaming.subscriptions.application.port.in.ReceiveNotificationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscriptions", description = "Subscription notification management")
public class NotificationController {

    private final ReceiveNotificationUseCase receiveNotificationUseCase;

    @Operation(
            summary = "Receive subscription notification",
            description = "Receives a notification and enqueues it in Kafka for asynchronous processing. " +
                    "The notification will update the subscription status according to its type."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Notification accepted and queued for processing"),
            @ApiResponse(responseCode = "400", description = "Invalid notification type or payload",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/notifications")
    public ResponseEntity<Void> receiveNotification(@Valid @RequestBody NotificationRequestDto request) {
        receiveNotificationUseCase.execute(request.toDomain());
        return ResponseEntity.accepted().build();
    }
}
