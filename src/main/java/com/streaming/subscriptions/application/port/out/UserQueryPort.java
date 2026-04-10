package com.streaming.subscriptions.application.port.out;

import com.streaming.subscriptions.domain.model.UserView;

import java.util.List;
import java.util.Optional;

/**
 * Porta somente-leitura para consultar usuários com informações de assinatura.
 */
public interface UserQueryPort {

    Optional<UserView> findById(Long id);

    List<UserView> findAll();
}
