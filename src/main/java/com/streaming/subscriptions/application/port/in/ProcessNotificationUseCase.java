package com.streaming.subscriptions.application.port.in;

import com.streaming.subscriptions.domain.model.Notification;

/**
 * Consome uma notificação da mensageria e atualiza o estado da assinatura e o histórico de auditoria.
 */
public interface ProcessNotificationUseCase {

    void execute(Notification notification);
}
