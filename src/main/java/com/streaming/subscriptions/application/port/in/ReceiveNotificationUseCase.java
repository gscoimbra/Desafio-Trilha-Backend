package com.streaming.subscriptions.application.port.in;

import com.streaming.subscriptions.domain.model.Notification;

/**
 * Recebe uma notificação e enfileira.
 */
public interface ReceiveNotificationUseCase {

    void execute(Notification notification);
}
