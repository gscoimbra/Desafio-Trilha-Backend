package com.streaming.subscriptions.adapter.out.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.streaming.subscriptions.application.port.in.ProcessNotificationUseCase;
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

    @KafkaListener(
            topics = "${app.kafka.topics.subscription-notifications}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String payload) {
        try {
            KafkaNotificationPayload parsed = objectMapper.readValue(payload, KafkaNotificationPayload.class);
            processNotificationUseCase.execute(parsed.toDomain());
        } catch (SubscriptionNotFoundException e) {
            log.warn("Skipping message: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid notification message: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Failed to process subscription notification from Kafka", e);
            throw new RuntimeException("Kafka message processing failed", e);
        }
    }
}
