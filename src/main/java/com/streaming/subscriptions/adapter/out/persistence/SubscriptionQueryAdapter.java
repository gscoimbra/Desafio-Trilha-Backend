package com.streaming.subscriptions.adapter.out.persistence;

import com.streaming.subscriptions.adapter.out.persistence.entity.SubscriptionEntity;
import com.streaming.subscriptions.adapter.out.persistence.repository.SubscriptionJpaRepository;
import com.streaming.subscriptions.application.port.out.SubscriptionQueryPort;
import com.streaming.subscriptions.domain.model.SubscriptionView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class SubscriptionQueryAdapter implements SubscriptionQueryPort {

    private final SubscriptionJpaRepository subscriptionJpaRepository;

    @Override
    public Optional<SubscriptionView> findById(Long id) {
        return subscriptionJpaRepository.findById(id)
                .map(this::toView);
    }

    @Override
    public List<SubscriptionView> findAll() {
        return StreamSupport.stream(subscriptionJpaRepository.findAll().spliterator(), false)
                .map(this::toView)
                .toList();
    }

    @Override
    public List<SubscriptionView> findByUserId(Long userId) {
        return subscriptionJpaRepository.findByUser_IdOrderByIdAsc(userId).stream()
                .map(this::toView)
                .toList();
    }

    private SubscriptionView toView(SubscriptionEntity entity) {
        return new SubscriptionView(
                entity.getId(),
                entity.getUser().getId(),
                entity.getUser().getFullName(),
                entity.getStatus().getStatusName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
