package com.streaming.subscriptions.domain.exception;

public class InvalidNotificationTypeException extends DomainException {

    public InvalidNotificationTypeException(String rawType) {
        super(400, buildMessage(rawType));
    }

    private static String buildMessage(String rawType) {
        return "Invalid notification type: '%s'. Expected one of: SUBSCRIPTION_PURCHASED, SUBSCRIPTION_CANCELED, SUBSCRIPTION_RESTARTED."
                .formatted(rawType != null ? rawType : "null");
    }
}
