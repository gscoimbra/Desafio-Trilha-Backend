package com.streaming.subscriptions.application.port.out;

import java.util.Optional;

/**
 * Lookup of status identifiers by canonical name (e.g. {@code ativa}, {@code cancelada}).
 */
public interface StatusRepositoryPort {

    Optional<Long> findIdByStatusName(String statusName);
}
