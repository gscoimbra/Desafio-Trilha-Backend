package com.streaming.subscriptions.application.port.in;

import com.streaming.subscriptions.domain.model.Notification;

/**
 * Receives a subscription notification from the HTTP API and enqueues it for async processing.
 */
public interface ReceiveNotificationUseCase {

    void execute(Notification notification);
}
