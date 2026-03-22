package com.streaming.subscriptions.application.port.in;

import com.streaming.subscriptions.domain.model.UserView;

/**
 * Use case for creating a user with an initial subscription (cancelada).
 */
public interface CreateUserUseCase {

    UserView execute(String fullName);
}
