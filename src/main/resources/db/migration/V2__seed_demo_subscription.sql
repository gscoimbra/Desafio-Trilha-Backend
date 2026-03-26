-- Dados de demonstração para E2E local (subscription id = 1 após inserts em DB vazio)
INSERT INTO users (full_name, created_at)
VALUES ('Demo User', NOW());

INSERT INTO subscriptions (user_id, status_id, created_at, updated_at)
VALUES (
    (SELECT id FROM users WHERE full_name = 'Demo User' ORDER BY id LIMIT 1),
    (SELECT id FROM status WHERE status_name = 'active' LIMIT 1),
    NOW(),
    NOW()
);
