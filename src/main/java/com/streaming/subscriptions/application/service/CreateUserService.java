package com.streaming.subscriptions.application.service;

import com.streaming.subscriptions.application.port.in.CreateUserUseCase;
import com.streaming.subscriptions.application.port.out.CreateUserPort;
import com.streaming.subscriptions.domain.model.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {

    private final CreateUserPort createUserPort;

    @Override
    @Transactional
    public UserView execute(String fullName) {
        return createUserPort.createUserWithSubscription(fullName);
    }
}
