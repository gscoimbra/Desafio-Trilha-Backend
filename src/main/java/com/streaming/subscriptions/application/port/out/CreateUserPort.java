package com.streaming.subscriptions.application.port.out;

import com.streaming.subscriptions.domain.model.UserView;

/**
 * Porta para criar um usuário com uma assinatura inicial (canceled).
 */
public interface CreateUserPort {

    UserView createUserWithSubscription(String fullName);
}

/**
 * Interface só de persistência; o service não sabe se é JPA, JDBC...
 */