package com.streaming.subscriptions.adapter.out.persistence;

import com.streaming.subscriptions.adapter.out.persistence.entity.SubscriptionEntity;
import com.streaming.subscriptions.domain.model.Subscription;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SubscriptionPersistenceMapper {

    public static Subscription toDomain(SubscriptionEntity entity) {
        return new Subscription(
                entity.getId(),
                entity.getUser().getId(),
                entity.getStatus().getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
