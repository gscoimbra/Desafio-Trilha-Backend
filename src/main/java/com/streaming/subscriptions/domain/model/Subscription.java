package com.streaming.subscriptions.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Representa o estado atual de uma assinatura.
 * Do ponto de vista do domínio, é a "unidade" que a aplicação carrega, altera e persiste.
 *
 * Como "muda de estado":
 * A alteração é feita via {@link #withStatus(Long, Instant)}.
 * Chega uma Notification → carrega Subscription → resolve o novo status → gera uma nova Subscription → persiste.
 * Ou seja, não se faz setStatusId; cria-se uma nova versão da assinatura com statusId e updatedAt atualizados.
 *
 * Subscription é o "estado atual", enquanto que Notification é o "evento que causa a mudança".
 */
public final class Subscription {

    private final Long id;

    // Dono da assinatura
    private final Long userId;
    
    // FK para a tabela status(numérico) - interessante porque no futuro se quiser renomear o texto do status_name(active/canceled) sem mexer nas assinaturas
    private final Long statusId;

    // Timestamps do registro
    private final Instant createdAt;
    private final Instant updatedAt;

    public Subscription(Long id, Long userId, Long statusId, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.userId = Objects.requireNonNull(userId, "userId");
        this.statusId = Objects.requireNonNull(statusId, "statusId");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
    }

    public Subscription withStatus(Long newStatusId, Instant newUpdatedAt) {
        return new Subscription(id, userId, newStatusId, createdAt, newUpdatedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}