package com.streaming.subscriptions.application.port.out;

import com.streaming.subscriptions.domain.model.Subscription;

import java.util.Optional;

/**
 * Persistência de assinaturas (estado atual).
 */
public interface SubscriptionRepositoryPort {

    Optional<Subscription> findById(Long id);

    void save(Subscription subscription);
}
