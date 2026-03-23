package com.streaming.subscriptions.application.port.out;

import com.streaming.subscriptions.domain.model.UserView;

/**
 * Port for creating a user with an initial subscription (cancelada).
 */
public interface CreateUserPort {

    UserView createUserWithSubscription(String fullName);
}
