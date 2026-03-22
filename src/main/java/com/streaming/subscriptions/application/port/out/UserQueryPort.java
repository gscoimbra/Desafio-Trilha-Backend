package com.streaming.subscriptions.application.port.out;

import com.streaming.subscriptions.domain.model.UserView;

import java.util.List;
import java.util.Optional;

/**
 * Read-only port for querying users with subscription info.
 */
public interface UserQueryPort {

    Optional<UserView> findById(Long id);

    List<UserView> findAll();
}
