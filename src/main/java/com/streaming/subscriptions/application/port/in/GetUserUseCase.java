package com.streaming.subscriptions.application.port.in;

import com.streaming.subscriptions.domain.model.UserView;

import java.util.List;
import java.util.Optional;

/**
 * Caso de uso para consultar usuários com informações de assinatura.
 */
public interface GetUserUseCase {

    Optional<UserView> getById(Long id); // Optional porque pode não existir um usuário com aquele Id. Serve para representar claramente "pode não existir" sem usar null.

    List<UserView> listAll(); // Caso não haja usuários, retorna uma lista vazia.
}
