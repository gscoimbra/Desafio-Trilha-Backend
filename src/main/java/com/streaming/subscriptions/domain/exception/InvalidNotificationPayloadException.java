package com.streaming.subscriptions.domain.exception;

/**
 * Conteúdo de notificação malformado ou inválido.
 */
public class InvalidNotificationPayloadException extends DomainException {

    public InvalidNotificationPayloadException(String message) {
        super(400, message);
    }

    public InvalidNotificationPayloadException(String message, Throwable cause) {
        super(400, message, cause);
    }
}
