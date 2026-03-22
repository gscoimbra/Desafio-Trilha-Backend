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

| Camada    | Descrição                                                        |
|-----------|------------------------------------------------------------------|
| Domain    | `Subscription`, `Notification`, exceções, enums                  |
| Application | Use cases e serviços (`ProcessNotificationService`, etc.)      |
| Adapters In | REST (`NotificationController`, `SubscriptionController`, `UserController`), Kafka Consumer |
| Adapters Out | PostgreSQL (JPA), Kafka Producer                               |

Fluxo: **REST → Kafka → Consumer → Processamento → DB**

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

### `GET /api/subscriptions` e `GET /api/subscriptions/{id}`

Consultar assinaturas.

- **`GET /api/subscriptions`** – lista todas ou filtra por `?userId=1`
- **`GET /api/subscriptions/{id}`** – retorna detalhes (id, userId, userName, statusName, createdAt, updatedAt)

**Exemplo de resposta (200):**
```json
{
  "id": 1,
  "userId": 1,
  "userName": "Demo User",
  "statusName": "ativa",
  "createdAt": "2025-01-01T12:00:00Z",
  "updatedAt": "2025-02-16T12:00:00Z"
}
```

- **404 Not Found** – assinatura inexistente

---

### `POST /api/subscriptions/notifications`

Recebe notificações e enfileira para processamento assíncrono.

**Request:**
```json
{
  "subscriptionId": 1,
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
- `404 Not Found` – assinatura inexistente (no processamento assíncrono)

---

## Testes

```bash
./mvnw test
# ou: .\mvnw.cmd test   (Windows)
```

- **Unitários:** `ProcessNotificationServiceTest`, `ReceiveNotificationServiceTest`
- **Controller:** `NotificationControllerTest`, `SubscriptionControllerTest`, `UserControllerTest` (MockMvc)
- **Integração:** `ProcessNotificationIntegrationTest`, `CreateUserIntegrationTest` (H2, fluxo completo)

---

## Documentação Adicional

| Arquivo      | Conteúdo                                                |
|-------------|----------------------------------------------------------|
| `COMMENTS.md` | Decisões arquiteturais, convenções e notas técnicas    |
| `HISTORY.md`  | Histórico de versões e fases do desafio                |