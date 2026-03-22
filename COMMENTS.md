# Comentários e Notas Técnicas

Documento de decisões arquiteturais, convenções e observações do projeto.

---

## Arquitetura Hexagonal

O serviço segue **Ports and Adapters** (Clean Architecture):

- **Domain:** Entidades (`Subscription`, `Notification`), exceções e enums imutáveis.
- **Application (Use Cases):** Casos de uso (`ReceiveNotificationUseCase`, `ProcessNotificationUseCase`) e serviços que orquestram a lógica.
- **Ports (Interfaces):** `*RepositoryPort`, `MessagePublisherPort` — contratos do domínio.
- **Adapters (Implementações):**
  - **In:** REST (`NotificationController`, `UserController`), Kafka Consumer (`SubscriptionNotificationConsumer`).
  - **Out:** PostgreSQL (JPA), Kafka Producer, Flyway.

O domínio não depende de frameworks. As dependências apontam para dentro (regra de dependência).

---

## Fluxo de Notificações

1. **REST → Kafka:** `POST /api/subscriptions/notifications` recebe o payload, valida e publica no tópico `subscription-notifications`.
2. **Kafka → Processamento:** Consumer consome a mensagem, converte para `Notification` e chama `ProcessNotificationService`.
3. **Processamento:** O serviço busca a assinatura, resolve o novo status (ativa/cancelada), atualiza e persiste no `event_history`.

Tudo é assíncrono após o retorno 202 Accepted.

---

## Mapeamento de Tipos

| NotificationType        | Status alvo  |
|-------------------------|--------------|
| SUBSCRIPTION_PURCHASED  | ativa        |
| SUBSCRIPTION_CANCELED   | cancelada    |

---

## Configurações Importantes

- **Flyway:** Migrações em `src/main/resources/db/migration/`. Em produção, `ddl-auto: validate`.
- **Kafka:** Tópico configurável via `app.kafka.topics.subscription-notifications`.
- **Perfil `test`:** H2, Kafka desabilitado, Flyway desabilitado, `ddl-auto: create-drop`, `defer-datasource-initialization: true` para `data.sql`.

---

## Perfis e Ambiente

- **Default:** PostgreSQL, Kafka, Flyway.
- **Test:** H2, sem Kafka, `data.sql` para seeds em testes de integração.
- **NoOpMessagePublisherAdapter:** Usado quando Kafka não está disponível (ex.: execução local sem `docker compose`). Ver `application.yml` e condicional de profile.

---

## Segurança e Produção

- Atualmente **não há autenticação/autorização** na API REST.
- Em produção, considerar: API Key, JWT, mTLS ou OAuth2.
- Kafka: avaliar SASL/SSL conforme política de segurança.

---

## Limitações Conhecidas

- Não há idempotência explícita: a mesma notificação processada duas vezes resulta em duas gravações em `event_history`.
- Não há retry configurado no consumer em caso de falha transacional.

---

## Convenções de Código

- DTOs com `@Valid`; erros mapeados em `RestExceptionHandler`.
- Exceções de domínio estendem `DomainException` e definem código HTTP.
- Repositórios expõem apenas o necessário via Ports; implementações em `adapter.out.persistence`.
