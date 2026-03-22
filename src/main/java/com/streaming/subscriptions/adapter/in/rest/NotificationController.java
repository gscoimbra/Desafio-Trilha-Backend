package com.streaming.subscriptions.adapter.in.rest;

import com.streaming.subscriptions.adapter.in.rest.dto.NotificationRequestDto;
import com.streaming.subscriptions.adapter.in.rest.error.ApiError;
import com.streaming.subscriptions.application.port.in.GetUserUseCase;
import com.streaming.subscriptions.application.port.in.ReceiveNotificationUseCase;
import com.streaming.subscriptions.domain.exception.UserNotFoundException;
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
@Tag(name = "Subscriptions", description = "Subscribe or unsubscribe users")
public class NotificationController {

    private final ReceiveNotificationUseCase receiveNotificationUseCase;
    private final GetUserUseCase getUserUseCase;

    @Operation(
            summary = "Subscribe or unsubscribe user",
            description = "Receives a request to subscribe (ativa) or unsubscribe (cancelada) a user. " +
                    "Enqueues for asynchronous processing."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Request accepted and queued"),
            @ApiResponse(responseCode = "400", description = "Invalid notification type or payload",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/notifications")
    public ResponseEntity<Void> receiveNotification(@Valid @RequestBody NotificationRequestDto request) {
        var user = getUserUseCase.getById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));
        var notification = request.toDomain(user.subscriptionId());
        receiveNotificationUseCase.execute(notification);
        return ResponseEntity.accepted().build();
    }
}
