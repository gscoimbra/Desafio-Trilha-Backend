package com.streaming.subscriptions.adapter.out.persistence;

import com.streaming.subscriptions.adapter.out.persistence.entity.StatusEntity;
import com.streaming.subscriptions.adapter.out.persistence.entity.SubscriptionEntity;
import com.streaming.subscriptions.adapter.out.persistence.repository.StatusJpaRepository;
import com.streaming.subscriptions.adapter.out.persistence.repository.SubscriptionJpaRepository;
import com.streaming.subscriptions.application.port.out.SubscriptionRepositoryPort;
import com.streaming.subscriptions.domain.model.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubscriptionRepositoryAdapter implements SubscriptionRepositoryPort {

    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final StatusJpaRepository statusJpaRepository;

    @Override
    public Optional<Subscription> findById(Long id) {
        return subscriptionJpaRepository.findById(id).map(SubscriptionPersistenceMapper::toDomain);
    }

    @Override
    public void save(Subscription subscription) {
        SubscriptionEntity entity = subscriptionJpaRepository.findById(subscription.getId())
                .orElseThrow(() -> new IllegalStateException("Subscription entity missing: " + subscription.getId()));
        StatusEntity status = statusJpaRepository.getReferenceById(subscription.getStatusId());
        entity.setStatus(status);
        entity.setUpdatedAt(subscription.getUpdatedAt());
        subscriptionJpaRepository.save(entity);
    }
}
