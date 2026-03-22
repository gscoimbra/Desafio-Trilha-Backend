package com.streaming.subscriptions.application.service;

import com.streaming.subscriptions.application.port.out.MessagePublisherPort;
import com.streaming.subscriptions.domain.model.Notification;
import com.streaming.subscriptions.domain.model.NotificationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReceiveNotificationServiceTest {

    @Mock
    private MessagePublisherPort messagePublisherPort;

    @InjectMocks
    private ReceiveNotificationService receiveNotificationService;

    @Test
    void execute_shouldPublishNotificationToPort() {
        Notification notification = new Notification(1L, NotificationType.SUBSCRIPTION_PURCHASED, Instant.now());

        receiveNotificationService.execute(notification);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(messagePublisherPort).publish(captor.capture());
        assertThat(captor.getValue().getSubscriptionId()).isEqualTo(1L);
        assertThat(captor.getValue().getType()).isEqualTo(NotificationType.SUBSCRIPTION_PURCHASED);
    }
}
