package com.streaming.subscriptions.application.service;

import com.streaming.subscriptions.application.port.in.GetSubscriptionUseCase;
import com.streaming.subscriptions.application.port.out.SubscriptionQueryPort;
import com.streaming.subscriptions.domain.exception.SubscriptionNotFoundException;
import com.streaming.subscriptions.domain.model.SubscriptionView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetSubscriptionService implements GetSubscriptionUseCase {

    private final SubscriptionQueryPort subscriptionQueryPort;

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionView> getById(Long id) {
        return subscriptionQueryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionView> listAll() {
        return subscriptionQueryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionView> listByUserId(Long userId) {
        return subscriptionQueryPort.findByUserId(userId);
    }
}
