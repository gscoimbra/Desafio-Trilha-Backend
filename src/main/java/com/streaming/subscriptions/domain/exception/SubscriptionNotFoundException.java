package com.streaming.subscriptions.domain.exception;

public class SubscriptionNotFoundException extends DomainException {

    public SubscriptionNotFoundException(Long subscriptionId) {
        super(404, "Subscription not found: " + subscriptionId);
    }
}
