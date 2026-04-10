package com.streaming.subscriptions.adapter.out.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.streaming.subscriptions.application.port.in.ProcessNotificationUseCase;
import com.streaming.subscriptions.domain.exception.DomainException;
import com.streaming.subscriptions.domain.exception.SubscriptionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@Slf4j
@RequiredArgsConstructor
public class SubscriptionNotificationConsumer {

    private final ObjectMapper objectMapper;
    private final ProcessNotificationUseCase processNotificationUseCase;

    // Escuta o Kafka
    @KafkaListener(
            topics = "${app.kafka.topics.subscription-notifications}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String payload) {
        try {
            KafkaNotificationPayload parsed = objectMapper.readValue(payload, KafkaNotificationPayload.class); // Aqui o objectMapper desserializa a String(JSON bruto) que o Kafak entrega.
            processNotificationUseCase.execute(parsed.toDomain()); // parsed.toDomain() -> converte o DTO de "transporte"(enviado pelo Kafka) para o modelo de domínio(Notification(Com NotificationType, Instant...))
        } catch (SubscriptionNotFoundException error) {
            log.warn("Skipping message: {}", error.getMessage());
        } catch (DomainException error) {
            if (error.isServerError()) {
                log.error("Server-side error while processing Kafka message: {}", error.getMessage(), error);
                throw new RuntimeException("Kafka message processing failed", error);
            }
            log.warn("Invalid notification message: {}", error.getMessage());
        } catch (Exception error) {
            log.error("Failed to process subscription notification from Kafka", error);
            throw new RuntimeException("Kafka message processing failed", error);
        }
    }
}
