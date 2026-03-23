package com.streaming.subscriptions.adapter.out.persistence;

import com.streaming.subscriptions.adapter.out.persistence.repository.StatusJpaRepository;
import com.streaming.subscriptions.application.port.out.StatusRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StatusRepositoryAdapter implements StatusRepositoryPort {

    private final StatusJpaRepository statusJpaRepository;

    @Override
    public Optional<Long> findIdByStatusName(String statusName) {
        return statusJpaRepository.findByStatusName(statusName).map(s -> s.getId());
    }
}
