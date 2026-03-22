# Desafio Trilha Backend

Serviço de notificações de assinaturas de streaming (Java 17, Spring Boot 3.2, Kafka, PostgreSQL), com arquitetura hexagonal.

---

## Quick Start

**Pré-requisitos:** Java 17, Docker, Python 3, Git. Opcional: Make.

```bash
# 1. Subir PostgreSQL e Kafka
make up
# ou: docker compose up -d

# 2. Rodar a aplicação (em outro terminal)
make run
# ou: .\mvnw.cmd spring-boot:run   (Windows)

# 3. Enviar notificações de teste (fluxo: PURCHASED -> CANCELED -> PURCHASED)
make sendNotifications
# ou: python scripts/send_notifications.py
# ou (Windows): powershell -ExecutionPolicy Bypass -File scripts/send_notifications.ps1
```

**Variável de ambiente:** `JAVA_HOME` deve apontar para o JDK 17.

- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html

---

## Arquitetura

O projeto usa **Ports and Adapters** (Hexagonal):

- **Domain:** Entidades (`Subscription`, `Notification`), exceções e enums imutáveis
- **Application:** Casos de uso (`ReceiveNotificationUseCase`, `ProcessNotificationUseCase`, `CreateUserUseCase`, `GetUserUseCase`) e serviços
- **Ports:** `*RepositoryPort`, `MessagePublisherPort`, `CreateUserPort`, `UserQueryPort`
- **Adapters In:** REST (`NotificationController`, `UserController`), Kafka Consumer (`SubscriptionNotificationConsumer`)
- **Adapters Out:** PostgreSQL (JPA), Kafka Producer, Flyway

O domínio não depende de frameworks. As dependências apontam para dentro.

**Fluxo de notificações:**
1. REST → Kafka: `POST /api/subscriptions/notifications` recebe userId e type, resolve subscriptionId do usuário e publica no tópico `subscription-notifications`
2. Kafka → Processamento: Consumer consome a mensagem, converte para `Notification` e chama `ProcessNotificationService`
3. Processamento: O serviço busca a assinatura, resolve o novo status (ativa/cancelada), atualiza e persiste no `event_history`

Tudo é assíncrono após o retorno 202 Accepted.

**Configurações:** Flyway em `db/migration/`; Kafka via `app.kafka.topics.subscription-notifications`; perfil `test` usa H2, sem Kafka, `data.sql` para seeds.

**Convenções:** DTOs com `@Valid`; erros em `RestExceptionHandler`; exceções de domínio estendem `DomainException`.

**Limitações:** Não há idempotência explícita; não há retry no consumer. Em produção, considerar autenticação (API Key, JWT, OAuth2) e SASL/SSL no Kafka.

---

## API

### `POST /api/users`

Cria um usuário com assinatura inicial em status cancelada.

**Request:**
```json
{
  "fullName": "Maria Silva"
}
```

**Resposta (201):**
```json
{
  "id": 1,
  "fullName": "Maria Silva",
  "createdAt": "2025-02-16T12:00:00Z",
  "subscriptionId": 1,
  "subscriptionStatus": "cancelada"
}
```

---

### `GET /api/users` e `GET /api/users/{id}`

Consultar usuários com status da assinatura.

- **`GET /api/users`** – lista todos os usuários
- **`GET /api/users/{id}`** – retorna detalhes (id, fullName, createdAt, subscriptionId, subscriptionStatus)

**Exemplo de resposta (200):**
```json
{
  "id": 1,
  "fullName": "Demo User",
  "createdAt": "2025-01-01T12:00:00Z",
  "subscriptionId": 1,
  "subscriptionStatus": "ativa"
}
```

- **404 Not Found** – usuário inexistente

---

### `POST /api/subscriptions/notifications`

Inscreve ou desinscreve um usuário (enfileira para processamento assíncrono).

**Request:**
```json
{
  "userId": 1,
  "type": "SUBSCRIPTION_PURCHASED"
}
```
O timestamp do evento é definido pelo servidor ao receber a requisição.

**Tipos:** `SUBSCRIPTION_PURCHASED`, `SUBSCRIPTION_CANCELED`

**Mapeamento:**
- PURCHASED → status `ativa`
- CANCELED → status `cancelada`

**Respostas:**
- `202 Accepted` – notificação aceita e enfileirada
- `400 Bad Request` – payload inválido ou tipo desconhecido
- `404 Not Found` – usuário inexistente

---

## Testes

```bash
./mvnw test
# ou: .\mvnw.cmd test   (Windows)
```

- **Unitários:** `ProcessNotificationServiceTest`, `ReceiveNotificationServiceTest`
- **Controller:** `NotificationControllerTest`, `UserControllerTest` (MockMvc)
- **Integração:** `ProcessNotificationIntegrationTest`, `CreateUserIntegrationTest` (H2, fluxo completo)