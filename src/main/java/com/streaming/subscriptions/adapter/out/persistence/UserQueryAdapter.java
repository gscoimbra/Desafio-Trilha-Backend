package com.streaming.subscriptions.adapter.out.persistence;

import com.streaming.subscriptions.adapter.out.persistence.entity.UserEntity;
import com.streaming.subscriptions.adapter.out.persistence.repository.UserJpaRepository;
import com.streaming.subscriptions.application.port.out.UserQueryPort;
import com.streaming.subscriptions.domain.model.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserView> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(this::toView); // "method reference", pegue cada UserEntity e chame o método toView nele"
    }

    @Override
    public List<UserView> findAll() {
        return userJpaRepository.findAll().stream()
                .map(this::toView)
                .toList();
    }

    private UserView toView(UserEntity user) {
        var subscription = user.getSubscriptions().stream().findFirst();
        Long subscriptionId = subscription.map(sub -> sub.getId()).orElse(null);
        String subscriptionStatus = subscription.map(sub -> sub.getStatus().getStatusName()).orElse(null);
        return new UserView(
                user.getId(),
                user.getFullName(),
                user.getCreatedAt(),
                subscriptionId, // ID da subscription do usuário(tabela subscriptions.id), não é o id do status(status.id)
                subscriptionStatus
        );
    }
}
