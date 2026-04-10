package com.streaming.subscriptions.domain.exception;

/**
 * Linha de status obrigatória ausente no banco de dados (problema de configuração / migração).
 */
public class StatusNotConfiguredException extends DomainException {

    public StatusNotConfiguredException(String statusName) {
        super(500, "Status not configured in database: " + statusName);
    }
}
