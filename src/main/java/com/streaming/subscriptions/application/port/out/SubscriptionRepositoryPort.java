package com.streaming.subscriptions.application.port.out;

import com.streaming.subscriptions.domain.model.Subscription;

import java.util.Optional;

/**
 * Persistence for subscriptions (current state).
 */
public interface SubscriptionRepositoryPort {

    Optional<Subscription> findById(Long id);

    void save(Subscription subscription);
}
