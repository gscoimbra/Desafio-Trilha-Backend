package com.streaming.subscriptions;

import com.streaming.subscriptions.adapter.out.persistence.repository.EventHistoryJpaRepository;
import com.streaming.subscriptions.adapter.out.persistence.repository.StatusJpaRepository;
import com.streaming.subscriptions.application.port.in.GetUserUseCase;
import com.streaming.subscriptions.application.port.in.ProcessNotificationUseCase;
import com.streaming.subscriptions.application.port.out.SubscriptionRepositoryPort;
import com.streaming.subscriptions.domain.model.Notification;
import com.streaming.subscriptions.domain.model.NotificationType;
import com.streaming.subscriptions.domain.model.Subscription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProcessNotificationIntegrationTest {

    @Autowired
    private ProcessNotificationUseCase processNotificationUseCase;

    @Autowired
    private GetUserUseCase getUserUseCase;

    @Autowired
    private SubscriptionRepositoryPort subscriptionRepository;

    @Autowired
    private StatusJpaRepository statusJpaRepository;

    @Autowired
    private EventHistoryJpaRepository eventHistoryJpaRepository;

    private static final Long SUBSCRIPTION_ID = 1L;

    @Test
    void processSubscriptionCanceled_shouldUpdateStatusToCancelada() {
        Subscription before = subscriptionRepository.findById(SUBSCRIPTION_ID).orElseThrow();
        Long canceladaId = statusJpaRepository.findByStatusName("cancelada").orElseThrow().getId();
        assertThat(before.getStatusId()).isNotEqualTo(canceladaId);

        Notification notification = new Notification(SUBSCRIPTION_ID, NotificationType.SUBSCRIPTION_CANCELED, Instant.now());
        processNotificationUseCase.execute(notification);

        Subscription after = subscriptionRepository.findById(SUBSCRIPTION_ID).orElseThrow();
        assertThat(after.getStatusId()).isEqualTo(canceladaId);
    }

    @Test
    void processSubscriptionPurchased_shouldUpdateStatusToAtiva() {
        // First cancel to have a non-ativa state
        processNotificationUseCase.execute(
                new Notification(SUBSCRIPTION_ID, NotificationType.SUBSCRIPTION_CANCELED, Instant.now())
        );
        Subscription afterCancel = subscriptionRepository.findById(SUBSCRIPTION_ID).orElseThrow();
        Long canceladaId = statusJpaRepository.findByStatusName("cancelada").orElseThrow().getId();
        assertThat(afterCancel.getStatusId()).isEqualTo(canceladaId);

        // Then restart
        processNotificationUseCase.execute(
                new Notification(SUBSCRIPTION_ID, NotificationType.SUBSCRIPTION_PURCHASED, Instant.now())
        );
        Subscription afterRestart = subscriptionRepository.findById(SUBSCRIPTION_ID).orElseThrow();
        Long ativaId = statusJpaRepository.findByStatusName("ativa").orElseThrow().getId();
        assertThat(afterRestart.getStatusId()).isEqualTo(ativaId);
    }

    @Test
    void processNotification_shouldAppendEventToHistory() {
        long countBefore = eventHistoryJpaRepository.count();

        processNotificationUseCase.execute(
                new Notification(SUBSCRIPTION_ID, NotificationType.SUBSCRIPTION_PURCHASED, Instant.now())
        );

        long countAfter = eventHistoryJpaRepository.count();
        assertThat(countAfter).isEqualTo(countBefore + 1);
    }

    @Test
    void getUserById_shouldReturnDetailsWithSubscriptionStatus() {
        var view = getUserUseCase.getById(1L).orElseThrow();
        assertThat(view.id()).isEqualTo(1L);
        assertThat(view.fullName()).isEqualTo("Demo User");
        assertThat(view.subscriptionStatus()).isIn("ativa", "cancelada");
        assertThat(view.subscriptionId()).isNotNull();
        assertThat(view.createdAt()).isNotNull();
    }
}
