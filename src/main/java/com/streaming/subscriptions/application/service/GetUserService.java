package com.streaming.subscriptions.application.service;

import com.streaming.subscriptions.application.port.in.GetUserUseCase;
import com.streaming.subscriptions.application.port.out.UserQueryPort;
import com.streaming.subscriptions.domain.model.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetUserService implements GetUserUseCase {

    private final UserQueryPort userQueryPort;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserView> getById(Long id) {
        return userQueryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserView> listAll() {
        return userQueryPort.findAll();
    }
}
