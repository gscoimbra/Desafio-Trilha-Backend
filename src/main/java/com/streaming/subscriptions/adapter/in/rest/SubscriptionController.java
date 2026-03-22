package com.streaming.subscriptions.adapter.in.rest;

import com.streaming.subscriptions.adapter.in.rest.error.ApiError;
import com.streaming.subscriptions.application.port.in.GetSubscriptionUseCase;
import com.streaming.subscriptions.domain.exception.SubscriptionNotFoundException;
import com.streaming.subscriptions.domain.model.SubscriptionView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscriptions", description = "Subscription query and notification management")
public class SubscriptionController {

    private final GetSubscriptionUseCase getSubscriptionUseCase;

    @Operation(summary = "Get subscription by ID", description = "Returns subscription details including user name and status.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subscription found"),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionView> getById(
            @Parameter(description = "Subscription ID") @PathVariable Long id) {
        SubscriptionView view = getSubscriptionUseCase.getById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
        return ResponseEntity.ok(view);
    }

    @Operation(
            summary = "List subscriptions",
            description = "Returns all subscriptions or filters by userId when the query parameter is provided."
    )
    @ApiResponse(responseCode = "200", description = "List of subscriptions")
    @GetMapping
    public ResponseEntity<List<SubscriptionView>> list(
            @Parameter(description = "Optional filter by user ID") @RequestParam(required = false) Long userId) {
        List<SubscriptionView> list = userId != null
                ? getSubscriptionUseCase.listByUserId(userId)
                : getSubscriptionUseCase.listAll();
        return ResponseEntity.ok(list);
    }
}
