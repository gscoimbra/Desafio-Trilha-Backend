package com.streaming.subscriptions.application.port.out;

import com.streaming.subscriptions.domain.model.Notification;

/**
 * Publica uma notificação na camada de mensageria.
 */
public interface MessagePublisherPort {

    void publish(Notification notification);
}
