package com.streaming.subscriptions.application.service;

import com.streaming.subscriptions.application.port.in.ReceiveNotificationUseCase;
import com.streaming.subscriptions.application.port.out.MessagePublisherPort;
import com.streaming.subscriptions.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiveNotificationService implements ReceiveNotificationUseCase {

    private final MessagePublisherPort messagePublisherPort;

    @Override
    public void execute(Notification notification) {
        messagePublisherPort.publish(notification);
    }
}

/**
 * A lógica aqui é delegar para a porta de mensageria, note que o service não conhece o Kafka,
 * quem conhece é só MessagePubliserPort
 */
