package com.streaming.subscriptions.adapter.out.persistence;

import com.streaming.subscriptions.adapter.out.persistence.entity.EventHistoryEntity;
import com.streaming.subscriptions.adapter.out.persistence.entity.SubscriptionEntity;
import com.streaming.subscriptions.adapter.out.persistence.repository.EventHistoryJpaRepository;
import com.streaming.subscriptions.adapter.out.persistence.repository.SubscriptionJpaRepository;
import com.streaming.subscriptions.application.port.out.EventHistoryRepositoryPort;
import com.streaming.subscriptions.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventHistoryRepositoryAdapter implements EventHistoryRepositoryPort {

    private final EventHistoryJpaRepository eventHistoryJpaRepository;
    private final SubscriptionJpaRepository subscriptionJpaRepository;

    @Override
    public void append(Notification notification) {
        SubscriptionEntity subscription = subscriptionJpaRepository
                .findById(notification.getSubscriptionId())
                .orElseThrow(() -> new IllegalStateException(
                        "Cannot append event: subscription " + notification.getSubscriptionId() + " not found"));

        EventHistoryEntity entity = EventHistoryEntity.builder()
                .subscription(subscription)
                .type(notification.getType().name())
                .createdAt(notification.getOccurredAt())
                .build();

        eventHistoryJpaRepository.save(entity);
    }
}
