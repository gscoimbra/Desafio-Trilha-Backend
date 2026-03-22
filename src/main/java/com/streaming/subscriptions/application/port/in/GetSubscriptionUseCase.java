package com.streaming.subscriptions.application.port.in;

import com.streaming.subscriptions.domain.model.SubscriptionView;

import java.util.List;
import java.util.Optional;

/**
 * Use case for querying subscriptions.
 */
public interface GetSubscriptionUseCase {

    Optional<SubscriptionView> getById(Long id);

    List<SubscriptionView> listAll();

    List<SubscriptionView> listByUserId(Long userId);
}
