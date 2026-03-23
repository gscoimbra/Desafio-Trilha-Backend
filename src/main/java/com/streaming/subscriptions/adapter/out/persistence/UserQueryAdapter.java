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
                .map(this::toView);
    }

    @Override
    public List<UserView> findAll() {
        return userJpaRepository.findAll().stream()
                .map(this::toView)
                .toList();
    }

    private UserView toView(UserEntity entity) {
        var sub = entity.getSubscriptions().stream().findFirst();
        Long subId = sub.map(s -> s.getId()).orElse(null);
        String subStatus = sub.map(s -> s.getStatus().getStatusName()).orElse(null);
        return new UserView(
                entity.getId(),
                entity.getFullName(),
                entity.getCreatedAt(),
                subId,
                subStatus
        );
    }
}
