package com.streaming.subscriptions.adapter.out.persistence.repository;

import com.streaming.subscriptions.adapter.out.persistence.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {

    List<SubscriptionEntity> findByUser_IdOrderByIdAsc(Long userId);
}
