package com.streaming.subscriptions.application.service;

import com.streaming.subscriptions.application.port.out.EventHistoryRepositoryPort;
import com.streaming.subscriptions.application.port.out.StatusRepositoryPort;
import com.streaming.subscriptions.application.port.out.SubscriptionRepositoryPort;
import com.streaming.subscriptions.domain.exception.StatusNotConfiguredException;
import com.streaming.subscriptions.domain.exception.SubscriptionNotFoundException;
import com.streaming.subscriptions.domain.model.Notification;
import com.streaming.subscriptions.domain.model.NotificationType;
import com.streaming.subscriptions.domain.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessNotificationServiceTest {

    @Mock
    private SubscriptionRepositoryPort subscriptionRepository;

    @Mock
    private StatusRepositoryPort statusRepository;

    @Mock
    private EventHistoryRepositoryPort eventHistoryRepository;

    @InjectMocks
    private ProcessNotificationService processNotificationService;

    private static final Long SUBSCRIPTION_ID = 1L;
    private static final Long USER_ID = 10L;
    private static final Long STATUS_ATIVA_ID = 1L;
    private static final Long STATUS_CANCELADA_ID = 2L;

    private Subscription subscription;
    private Notification notification;

    @BeforeEach
    void setUp() {
        Instant now = Instant.now();
        subscription = new Subscription(SUBSCRIPTION_ID, USER_ID, STATUS_ATIVA_ID, now, now);
        notification = new Notification(SUBSCRIPTION_ID, NotificationType.SUBSCRIPTION_CANCELED, now);
    }

    @Test
    void execute_whenSubscriptionExists_shouldUpdateStatusAndAppendEvent() {
        when(subscriptionRepository.findById(SUBSCRIPTION_ID)).thenReturn(Optional.of(subscription));
        when(statusRepository.findIdByStatusName("cancelada")).thenReturn(Optional.of(STATUS_CANCELADA_ID));

        processNotificationService.execute(notification);

        ArgumentCaptor<Subscription> savedSubscription = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository).save(savedSubscription.capture());
        assertThat(savedSubscription.getValue().getStatusId()).isEqualTo(STATUS_CANCELADA_ID);

        verify(eventHistoryRepository).append(notification);
    }

    @Test
    void execute_whenSubscriptionNotFound_shouldThrowSubscriptionNotFoundException() {
        when(subscriptionRepository.findById(SUBSCRIPTION_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> processNotificationService.execute(notification))
                .isInstanceOf(SubscriptionNotFoundException.class)
                .hasMessageContaining(String.valueOf(SUBSCRIPTION_ID));

        verify(subscriptionRepository, never()).save(any());
        verify(eventHistoryRepository, never()).append(any());
    }

    @Test
    void execute_whenStatusNotConfigured_shouldThrowStatusNotConfiguredException() {
        when(subscriptionRepository.findById(SUBSCRIPTION_ID)).thenReturn(Optional.of(subscription));
        when(statusRepository.findIdByStatusName("cancelada")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> processNotificationService.execute(notification))
                .isInstanceOf(StatusNotConfiguredException.class)
                .hasMessageContaining("cancelada");

        verify(subscriptionRepository, never()).save(any());
        verify(eventHistoryRepository, never()).append(any());
    }

    @Test
    void execute_whenSubscriptionPurchased_shouldSetStatusAtiva() {
        notification = new Notification(SUBSCRIPTION_ID, NotificationType.SUBSCRIPTION_PURCHASED, Instant.now());
        when(subscriptionRepository.findById(SUBSCRIPTION_ID)).thenReturn(Optional.of(subscription));
        when(statusRepository.findIdByStatusName("ativa")).thenReturn(Optional.of(STATUS_ATIVA_ID));

        processNotificationService.execute(notification);

        ArgumentCaptor<Subscription> savedSubscription = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository).save(savedSubscription.capture());
        assertThat(savedSubscription.getValue().getStatusId()).isEqualTo(STATUS_ATIVA_ID);
        verify(eventHistoryRepository).append(notification);
    }

    @Test
    void execute_whenSubscriptionRestarted_shouldSetStatusAtiva() {
        notification = new Notification(SUBSCRIPTION_ID, NotificationType.SUBSCRIPTION_RESTARTED, Instant.now());
        when(subscriptionRepository.findById(SUBSCRIPTION_ID)).thenReturn(Optional.of(subscription));
        when(statusRepository.findIdByStatusName("ativa")).thenReturn(Optional.of(STATUS_ATIVA_ID));

        processNotificationService.execute(notification);

        ArgumentCaptor<Subscription> savedSubscription = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository).save(savedSubscription.capture());
        assertThat(savedSubscription.getValue().getStatusId()).isEqualTo(STATUS_ATIVA_ID);
    }
}
