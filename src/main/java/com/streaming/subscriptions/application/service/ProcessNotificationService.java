package com.streaming.subscriptions.application.service;

import com.streaming.subscriptions.application.port.in.ProcessNotificationUseCase;
import com.streaming.subscriptions.application.port.out.EventHistoryRepositoryPort;
import com.streaming.subscriptions.application.port.out.StatusRepositoryPort;
import com.streaming.subscriptions.application.port.out.SubscriptionRepositoryPort;
import com.streaming.subscriptions.domain.exception.StatusNotConfiguredException;
import com.streaming.subscriptions.domain.exception.SubscriptionNotFoundException;
import com.streaming.subscriptions.domain.model.Notification;
import com.streaming.subscriptions.domain.model.NotificationType;
import com.streaming.subscriptions.domain.model.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProcessNotificationService implements ProcessNotificationUseCase {

    private final SubscriptionRepositoryPort subscriptionRepository;
    private final StatusRepositoryPort statusRepository;
    private final EventHistoryRepositoryPort eventHistoryRepository;

    @Override
    @Transactional
    public void execute(Notification notification) {
        Subscription subscription = subscriptionRepository
                .findById(notification.getSubscriptionId())
                .orElseThrow(() -> new SubscriptionNotFoundException(notification.getSubscriptionId()));

        String targetStatusName = resolveTargetStatusName(notification.getType());
        Long statusId = statusRepository.findIdByStatusName(targetStatusName)
                .orElseThrow(() -> new StatusNotConfiguredException(targetStatusName));

        Subscription updated = subscription.withStatus(statusId, Instant.now());
        subscriptionRepository.save(updated);
        eventHistoryRepository.append(notification);
    }

    private static String resolveTargetStatusName(NotificationType type) {
        return switch (type) {
            case SUBSCRIPTION_PURCHASED -> "ativa";
            case SUBSCRIPTION_CANCELED -> "cancelada";
        };
    }
}
