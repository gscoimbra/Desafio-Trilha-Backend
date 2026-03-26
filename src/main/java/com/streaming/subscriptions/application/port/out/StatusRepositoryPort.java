package com.streaming.subscriptions.application.port.out;

import java.util.Optional;

/**
 * Busca o identificador do status pelo nome canônico (ex.: {@code active}, {@code canceled}).
 */
public interface StatusRepositoryPort {

    Optional<Long> findIdByStatusName(String statusName);
}
