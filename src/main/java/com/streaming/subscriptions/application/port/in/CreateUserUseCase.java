package com.streaming.subscriptions.application.port.in;

import com.streaming.subscriptions.domain.model.UserView;

/**
 * Caso de uso para criar um usuário com uma assinatura inicial canceled.
 */
public interface CreateUserUseCase {

    UserView execute(String fullName);
}

/**
 * Define o que o sistema oferece: "criar usuário recebenfo full name e devolvendo UserView".
 */