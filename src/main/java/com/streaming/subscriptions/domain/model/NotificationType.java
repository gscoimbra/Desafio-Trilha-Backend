package com.streaming.subscriptions.domain.model;

/**
 * Enum do domínio.
 * Tipos de notificações do ciclo de vida da assinatura.
 */
public enum NotificationType {

    // Evento de compra da assinatura, no processamento vira status active.
    SUBSCRIPTION_PURCHASED,

    // Evento de cancelamento, no processamento vira status canceled.
    SUBSCRIPTION_CANCELED;

}

/**
 * Como os tipos são regras de negócio, precisam estar no domínio.
 * Garante que a aplicação use Enum ao invés de strings soltas
 */