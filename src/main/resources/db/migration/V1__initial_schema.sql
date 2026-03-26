-- Usuários (ERD: user; tabela 'users' evita a palavra reservada SQL 'user')
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Catálogo de status da assinatura (ex.: active, canceled)
CREATE TABLE status (
    id BIGSERIAL PRIMARY KEY,
    status_name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id),
    status_id BIGINT NOT NULL REFERENCES status (id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_subscriptions_user_id ON subscriptions (user_id);
CREATE INDEX idx_subscriptions_status_id ON subscriptions (status_id);

-- Trilha de auditoria dos eventos de notificação por assinatura
CREATE TABLE event_history (
    id BIGSERIAL PRIMARY KEY,
    subscription_id BIGINT NOT NULL REFERENCES subscriptions (id),
    type VARCHAR(64) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_event_history_subscription_id ON event_history (subscription_id);

-- Seed de status necessários para o processamento de notificações
INSERT INTO status (status_name) VALUES ('active');
INSERT INTO status (status_name) VALUES ('canceled');
