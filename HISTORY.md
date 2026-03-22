# Histórico do Projeto

Changelog e evolução do Desafio Trilha Backend.

---

## [0.0.1-SNAPSHOT] – Em desenvolvimento

### Fase 0 – Setup Inicial
- Projeto Spring Boot 3.2 com Java 17
- PostgreSQL, Flyway, Kafka
- Docker Compose para infraestrutura local

### Fase 1 – Schema e Migrações
- V1: Tabelas `users`, `status`, `subscriptions`, `event_history`
- Seed de status: `ativa`, `cancelada`
- V2: Dados de demonstração (usuário e assinatura id=1)

### Fase 2 – Domain
- Modelos: `Subscription`, `Notification`, `NotificationType`
- Exceções: `DomainException`, `SubscriptionNotFoundException`, `StatusNotConfiguredException`, `InvalidNotificationTypeException`, `InvalidNotificationPayloadException`

### Fase 3 – Ports (Interfaces)
- `ReceiveNotificationUseCase`, `ProcessNotificationUseCase`
- `SubscriptionRepositoryPort`, `StatusRepositoryPort`, `EventHistoryRepositoryPort`, `MessagePublisherPort`

### Fase 4 – REST API
- `NotificationController` com `POST /api/subscriptions/notifications`
- `NotificationRequestDto` com validação
- Retorno 202 Accepted

### Fase 5 – Processamento de Notificações
- `ProcessNotificationService`: atualiza status e append em `event_history`
- Mapeamento PURCHASED/RESTARTED → ativa, CANCELED → cancelada

### Fase 6 – Kafka Producer
- `KafkaMessagePublisherAdapter` publicando no tópico configurável
- `NoOpMessagePublisherAdapter` para ambiente sem Kafka

### Fase 7 – Kafka Consumer
- `SubscriptionNotificationConsumer` consumindo notificações
- `KafkaNotificationPayload` com validação do payload

### Fase 8 – Persistência
- Adapters JPA: `SubscriptionRepositoryAdapter`, `StatusRepositoryAdapter`, `EventHistoryRepositoryAdapter`

### Fase 9 – Tratamento de Erros
- `RestExceptionHandler` com `ApiError`
- Tratamento de `DomainException`, `MethodArgumentNotValidException`, `IllegalArgumentException`

### Fase 10 – OpenAPI / Swagger
- SpringDoc com documentação da API
- Swagger UI em `/swagger-ui.html`

### Fase 11 – Scripts de Teste
- `send_notifications.py` e `send_notifications.ps1` para envio de notificações de exemplo

### Fase 12 – Docker e Makefile
- `docker-compose.yml` com PostgreSQL, Zookeeper e Kafka
- `Makefile` com `up`, `down`, `run`, `sendNotifications`

### Fase 13 – Testes
- **Unitários:** `ProcessNotificationServiceTest`, `ReceiveNotificationServiceTest`
- **Controller:** `NotificationControllerTest` com MockMvc e `RestExceptionHandler`
- **Integração:** `ProcessNotificationIntegrationTest` com H2 e `data.sql`
- Configuração `application-test.yml` e `defer-datasource-initialization`

### Fase 14 – Documentação
- `COMMENTS.md`: decisões arquiteturais e notas técnicas
- `HISTORY.md`: changelog por fase
- `README.md`: documentação ampliada do projeto

### feat: endpoint de criação de usuário
- `POST /api/users` – cria usuário com assinatura inicial cancelada
- `CreateUserUseCase`, `CreateUserPort`, `UserView`, `UserController`
- `UserJpaRepository`, `CreateUserAdapter`

### fix: remoção de SUBSCRIPTION_RESTARTED
- Simplificado para dois estados: ativa (PURCHASED) e cancelada (CANCELED)
- Removido SUBSCRIPTION_RESTARTED do enum e mapeamento

### Endpoints de consulta (após Fase 14)
- `GET /api/subscriptions` – lista assinaturas (opcional `?userId=` para filtrar)
- `GET /api/subscriptions/{id}` – detalhes de uma assinatura (userName, statusName)
- `GetSubscriptionUseCase`, `SubscriptionQueryPort`, `SubscriptionView`, `SubscriptionController`
