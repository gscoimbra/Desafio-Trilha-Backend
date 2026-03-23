# Desafio Trilha Backend

Serviço de notificações de assinaturas de streaming (Java 17, Spring Boot 3.2, Kafka, PostgreSQL), com arquitetura hexagonal.

---

## Quick Start

**Pré-requisitos:** Java 17, Docker, Git.

```bash
# 1. Subir PostgreSQL e Kafka
docker compose up -d

# 2. Rodar a aplicação (em outro terminal)
.\mvnw.cmd spring-boot:run   # Windows
# ou: ./mvnw spring-boot:run   # Linux/Mac
```

**Variável de ambiente:** `JAVA_HOME` deve apontar para o JDK 17.

- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html

### Porta 8080 em uso

Se aparecer `Port 8080 was already in use`, outro processo Java está usando a porta. Para encerrar:

```powershell
Get-Process -Name java | Stop-Process -Force
```

Depois, rode a aplicação novamente.

---

## Arquitetura

O projeto usa **Ports and Adapters** (Hexagonal):

- **Domain:** Entidades (`Subscription`, `Notification`), exceções e enums imutáveis
- **Application:** Casos de uso (`ReceiveNotificationUseCase`, `ProcessNotificationUseCase`, `CreateUserUseCase`, `GetUserUseCase`) e serviços
- **Ports:** `*RepositoryPort`, `MessagePublisherPort`, `CreateUserPort`, `UserQueryPort`
- **Adapters In:** REST (`UserController`), Kafka Consumer (`SubscriptionNotificationConsumer`)
- **Adapters Out:** PostgreSQL (JPA), Kafka Producer, Flyway

O domínio não depende de frameworks. As dependências apontam para dentro.

**Fluxo de notificações:**
1. REST → Kafka: `POST /api/users/{id}/subscribe` ou `POST /api/users/{id}/unsubscribe` recebe o userId, resolve subscriptionId e publica no tópico `subscription-notifications`
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

### `POST /api/users/{id}/subscribe`

Ativa a assinatura do usuário (status `ativa`). Enfileira para processamento assíncrono.

- **202 Accepted** – requisição aceita
- **404 Not Found** – usuário inexistente

---

### `POST /api/users/{id}/unsubscribe`

Cancela a assinatura do usuário (status `cancelada`). Enfileira para processamento assíncrono.

- **202 Accepted** – requisição aceita
- **404 Not Found** – usuário inexistente

---

## Testes

```bash
./mvnw test
# ou: .\mvnw.cmd test   (Windows)
```

- **Unitários:** `ProcessNotificationServiceTest`, `ReceiveNotificationServiceTest`
- **Controller:** `UserControllerTest` (MockMvc)
- **Integração:** `ProcessNotificationIntegrationTest`, `CreateUserIntegrationTest` (H2, fluxo completo)