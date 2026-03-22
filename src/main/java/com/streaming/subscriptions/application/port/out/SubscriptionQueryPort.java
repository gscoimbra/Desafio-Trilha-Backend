package com.streaming.subscriptions.application.port.out;

import com.streaming.subscriptions.domain.model.SubscriptionView;

import java.util.List;
import java.util.Optional;

/**
 * Read-only port for querying subscription details.
 */
public interface SubscriptionQueryPort {

    Optional<SubscriptionView> findById(Long id);

    List<SubscriptionView> findAll();

    List<SubscriptionView> findByUserId(Long userId);
}
