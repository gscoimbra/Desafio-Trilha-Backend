package com.streaming.subscriptions.domain.model;

import java.time.Instant;

/**
 * Read model for created user (with subscription in cancelada).
 */
public record UserView(
        Long id,
        String fullName,
        Instant createdAt,
        Long subscriptionId,
        String subscriptionStatus
) {}
