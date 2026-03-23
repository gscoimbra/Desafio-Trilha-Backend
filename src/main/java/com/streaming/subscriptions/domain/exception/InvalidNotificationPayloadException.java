package com.streaming.subscriptions.domain.exception;

/**
 * Malformed or invalid notification content (e.g. Kafka message deserialization / validation).
 */
public class InvalidNotificationPayloadException extends DomainException {

    public InvalidNotificationPayloadException(String message) {
        super(400, message);
    }

    public InvalidNotificationPayloadException(String message, Throwable cause) {
        super(400, message, cause);
    }
}
