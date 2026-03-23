-- Demo data for local E2E (subscription id = 1 after inserts on empty DB)
INSERT INTO users (full_name, created_at)
VALUES ('Demo User', NOW());

INSERT INTO subscriptions (user_id, status_id, created_at, updated_at)
VALUES (
    (SELECT id FROM users WHERE full_name = 'Demo User' ORDER BY id LIMIT 1),
    (SELECT id FROM status WHERE status_name = 'ativa' LIMIT 1),
    NOW(),
    NOW()
);
