# Desafio-Trilha-Backend

Serviço de notificações de assinaturas de streaming (Java, Spring Boot, Kafka, PostgreSQL).

## Quick Start

**Pré-requisitos:** Java 17, Docker, Python 3, Git. Opcional: Make.

```bash
# 1. Subir PostgreSQL e Kafka
make up
# ou: docker compose up -d

# 2. Rodar a aplicação (em outro terminal)
make run
# ou: .\mvnw.cmd spring-boot:run   (Windows)

# 3. Enviar notificações de teste (fluxo: PURCHASED -> CANCELED -> RESTARTED)
make sendNotifications
# ou: python scripts/send_notifications.py
# ou (Windows): powershell -ExecutionPolicy Bypass -File scripts/send_notifications.ps1
```

**Variável de ambiente:** `JAVA_HOME` deve apontar para o JDK 17.

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html