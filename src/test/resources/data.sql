-- Seed data for integration tests (H2)
INSERT INTO status (status_name) VALUES ('ativa');
INSERT INTO status (status_name) VALUES ('cancelada');

INSERT INTO users (full_name, created_at) VALUES ('Demo User', CURRENT_TIMESTAMP);

INSERT INTO subscriptions (user_id, status_id, created_at, updated_at)
VALUES (
    (SELECT id FROM users LIMIT 1),
    (SELECT id FROM status WHERE status_name = 'ativa' LIMIT 1),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
