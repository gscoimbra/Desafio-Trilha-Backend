package com.streaming.subscriptions.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.streaming.subscriptions.application.port.out.MessagePublisherPort;
import com.streaming.subscriptions.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class KafkaMessagePublisherAdapter implements MessagePublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate; // API do Spring Kafka para enviar mensagens.
    private final ObjectMapper objectMapper; // Jackson: Converte objetos Java em JSON.

    @Value("${app.kafka.topics.subscription-notifications}") // Lê do application.yml o nome do tópico e guarda em topic.
    private String topic;

    @Override
    public void publish(Notification notification) {
        try {
            String payload = objectMapper.writeValueAsString(toMap(notification));
            kafkaTemplate.send(topic, String.valueOf(notification.getSubscriptionId()), payload);
        } catch (JsonProcessingException error) {
            throw new IllegalStateException("Failed to serialize notification for Kafka", error);
        }
    }

    private static Map<String, Object> toMap(Notification notification) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("subscriptionId", notification.getSubscriptionId());
        map.put("type", notification.getType().name());
        map.put("occurredAt", notification.getOccurredAt().toString());
        return map;
    }
}

/**
 * Esse adapter pega a Notificação, transforma em JSON no formato combinado
 * e manda para o tópico Kafka configurado.
 */