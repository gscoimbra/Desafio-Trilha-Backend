package com.streaming.subscriptions.adapter.in.rest;

import com.streaming.subscriptions.adapter.in.rest.dto.CreateUserRequestDto;
import com.streaming.subscriptions.adapter.in.rest.error.ApiError;
import com.streaming.subscriptions.application.port.in.CreateUserUseCase;
import com.streaming.subscriptions.application.port.in.GetUserUseCase;
import com.streaming.subscriptions.application.port.in.ReceiveNotificationUseCase;
import com.streaming.subscriptions.domain.exception.UserNotFoundException;
import com.streaming.subscriptions.domain.model.Notification;
import com.streaming.subscriptions.domain.model.NotificationType;
import com.streaming.subscriptions.domain.model.UserView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User creation, management and subscription")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final ReceiveNotificationUseCase receiveNotificationUseCase;

    @Operation(
            summary = "Create user",
            description = "Creates a user with an initial subscription in cancelada status."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid payload",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<UserView> createUser(@Valid @RequestBody CreateUserRequestDto request) {
        UserView view = createUserUseCase.execute(request.getFullName().trim());
        return ResponseEntity.status(201).body(view);
    }

    @Operation(summary = "Get user by ID", description = "Returns user details including subscription status.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserView> getById(
            @Parameter(description = "User ID") @PathVariable Long id) {
        UserView view = getUserUseCase.getById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok(view);
    }

    @Operation(summary = "List users", description = "Returns all users with subscription status.")
    @ApiResponse(responseCode = "200", description = "List of users")
    @GetMapping
    public ResponseEntity<List<UserView>> list() {
        return ResponseEntity.ok(getUserUseCase.listAll());
    }

    @Operation(
            summary = "Activate subscription",
            description = "Activates the user's subscription (ativa). Enqueues for asynchronous processing."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Request accepted and queued"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Void> subscribe(
            @Parameter(description = "User ID") @PathVariable Long id) {
        var user = getUserUseCase.getById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        var notification = new Notification(user.subscriptionId(), NotificationType.SUBSCRIPTION_PURCHASED, Instant.now());
        receiveNotificationUseCase.execute(notification);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Cancel subscription",
            description = "Cancels the user's subscription (cancelada). Enqueues for asynchronous processing."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Request accepted and queued"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/{id}/unsubscribe")
    public ResponseEntity<Void> unsubscribe(
            @Parameter(description = "User ID") @PathVariable Long id) {
        var user = getUserUseCase.getById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        var notification = new Notification(user.subscriptionId(), NotificationType.SUBSCRIPTION_CANCELED, Instant.now());
        receiveNotificationUseCase.execute(notification);
        return ResponseEntity.accepted().build();
    }
}
