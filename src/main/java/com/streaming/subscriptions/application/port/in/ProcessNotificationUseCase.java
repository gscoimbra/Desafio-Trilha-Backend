package com.streaming.subscriptions.application.port.in;

import com.streaming.subscriptions.domain.model.Notification;

/**
 * Consumes a notification from messaging and updates subscription state + audit history.
 */
public interface ProcessNotificationUseCase {

    void execute(Notification notification);
}
