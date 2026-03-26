-- Uma assinatura por usuário (regra de negócio)
ALTER TABLE subscriptions ADD CONSTRAINT uk_subscriptions_user_id UNIQUE (user_id);
