package com.streaming.subscriptions.adapter.out.persistence.repository;

import com.streaming.subscriptions.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
