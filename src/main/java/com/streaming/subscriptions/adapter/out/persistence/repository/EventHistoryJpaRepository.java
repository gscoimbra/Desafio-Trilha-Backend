package com.streaming.subscriptions.adapter.out.persistence.repository;

import com.streaming.subscriptions.adapter.out.persistence.entity.EventHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventHistoryJpaRepository extends JpaRepository<EventHistoryEntity, Long> {
}
