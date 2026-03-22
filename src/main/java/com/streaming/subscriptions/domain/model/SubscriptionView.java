package com.streaming.subscriptions.domain.model;

import java.time.Instant;

/**
 * Read model for subscription queries (id, user, status, timestamps).
 */
public record SubscriptionView(
        Long id,
        Long userId,
        String userName,
        String statusName,
        Instant createdAt,
        Instant updatedAt
) {}
