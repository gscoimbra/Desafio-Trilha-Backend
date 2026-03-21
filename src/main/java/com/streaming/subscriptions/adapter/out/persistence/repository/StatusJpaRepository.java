package com.streaming.subscriptions.adapter.out.persistence.repository;

import com.streaming.subscriptions.adapter.out.persistence.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusJpaRepository extends JpaRepository<StatusEntity, Long> {

    Optional<StatusEntity> findByStatusName(String statusName);
}
