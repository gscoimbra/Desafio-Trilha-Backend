-- Users (ERD: user; table name avoids SQL reserved word "user")
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Subscription status lookup (e.g. ativa, cancelada)
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

-- Audit trail of notification events per subscription
CREATE TABLE event_history (
    id BIGSERIAL PRIMARY KEY,
    subscription_id BIGINT NOT NULL REFERENCES subscriptions (id),
    type VARCHAR(64) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_event_history_subscription_id ON event_history (subscription_id);

-- Seed statuses required by notification processing
INSERT INTO status (status_name) VALUES ('ativa');
INSERT INTO status (status_name) VALUES ('cancelada');
