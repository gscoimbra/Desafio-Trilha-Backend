package com.streaming.subscriptions.domain.model;

/**
 * Types of subscription lifecycle notifications received via HTTP and processed asynchronously.
 */
public enum NotificationType {

    SUBSCRIPTION_PURCHASED,
    SUBSCRIPTION_CANCELED,
    SUBSCRIPTION_RESTARTED;

    public static boolean isValid(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            NotificationType.valueOf(value.trim());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
