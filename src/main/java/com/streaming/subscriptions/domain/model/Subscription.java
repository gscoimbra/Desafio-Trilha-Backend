package com.streaming.subscriptions.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Subscription aggregate root (read/update via persistence port).
 */
public final class Subscription {

    private final Long id;
    private final Long userId;
    private final Long statusId;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Subscription(Long id, Long userId, Long statusId, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.userId = Objects.requireNonNull(userId, "userId");
        this.statusId = Objects.requireNonNull(statusId, "statusId");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
    }

    public Subscription withStatus(Long newStatusId, Instant newUpdatedAt) {
        return new Subscription(id, userId, newStatusId, createdAt, newUpdatedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
