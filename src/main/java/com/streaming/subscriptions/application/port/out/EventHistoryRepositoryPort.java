package com.streaming.subscriptions.application.port.out;

import com.streaming.subscriptions.domain.model.Notification;

/**
 * Persists notification events for audit per subscription.
 */
public interface EventHistoryRepositoryPort {

    void append(Notification notification);
}
