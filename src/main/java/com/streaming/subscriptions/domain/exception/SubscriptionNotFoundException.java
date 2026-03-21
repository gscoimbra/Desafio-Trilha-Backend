package com.streaming.subscriptions.domain.exception;

public class SubscriptionNotFoundException extends RuntimeException {

    public SubscriptionNotFoundException(Long subscriptionId) {
        super("Subscription not found: " + subscriptionId);
    }
}
