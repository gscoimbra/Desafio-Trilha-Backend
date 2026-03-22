.PHONY: up down run sendNotifications help

# Detect OS for Maven wrapper
ifeq ($(OS),Windows_NT)
    MVNW := .\mvnw.cmd
else
    MVNW := ./mvnw
endif

help:
	@echo "Available targets:"
	@echo "  make up              - Start PostgreSQL and Kafka (docker compose up -d)"
	@echo "  make down            - Stop containers (docker compose down)"
	@echo "  make run             - Run the application"
	@echo "  make sendNotifications - Send subscription lifecycle notifications to the API"
	@echo ""
	@echo "Recommended flow: make up, then make run (in another terminal), then make sendNotifications"

up:
	docker compose up -d

down:
	docker compose down

run:
	$(MVNW) spring-boot:run

sendNotifications:
	@python scripts/send_notifications.py 2>/dev/null || python3 scripts/send_notifications.py 2>/dev/null || powershell -ExecutionPolicy Bypass -File scripts/send_notifications.ps1
