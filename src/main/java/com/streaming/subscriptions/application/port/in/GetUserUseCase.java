package com.streaming.subscriptions.application.port.in;

import com.streaming.subscriptions.domain.model.UserView;

import java.util.List;
import java.util.Optional;

/**
 * Use case for querying users with subscription info.
 */
public interface GetUserUseCase {

    Optional<UserView> getById(Long id);

    List<UserView> listAll();
}
