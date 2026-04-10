package com.streaming.subscriptions.adapter.out.persistence;

import com.streaming.subscriptions.adapter.out.persistence.entity.StatusEntity;
import com.streaming.subscriptions.adapter.out.persistence.entity.SubscriptionEntity;
import com.streaming.subscriptions.adapter.out.persistence.entity.UserEntity;
import com.streaming.subscriptions.adapter.out.persistence.repository.StatusJpaRepository;
import com.streaming.subscriptions.adapter.out.persistence.repository.SubscriptionJpaRepository;
import com.streaming.subscriptions.adapter.out.persistence.repository.UserJpaRepository;
import com.streaming.subscriptions.application.port.out.CreateUserPort;
import com.streaming.subscriptions.domain.exception.StatusNotConfiguredException;
import com.streaming.subscriptions.domain.model.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class CreateUserAdapter implements CreateUserPort {

    private final UserJpaRepository userJpaRepository;
    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final StatusJpaRepository statusJpaRepository;

    @Override
    public UserView createUserWithSubscription(String fullName) {
        Instant now = Instant.now(); // Captura o "agora" em UTC
        UserEntity user = UserEntity.builder()
                .fullName(fullName)
                .createdAt(now)
                .build();
        user = userJpaRepository.save(user);

        StatusEntity canceled = statusJpaRepository.findByStatusName("canceled")
                .orElseThrow(() -> new StatusNotConfiguredException("canceled"));

        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .user(user)
                .status(canceled)
                .createdAt(now)
                .updatedAt(now)
                .build();
        subscription = subscriptionJpaRepository.save(subscription);

        return new UserView(
                user.getId(),
                user.getFullName(),
                user.getCreatedAt(),
                subscription.getId(),
                canceled.getStatusName()
        );
    }
}
