package com.streaming.subscriptions.adapter.out.persistence.repository;

import com.streaming.subscriptions.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"subscriptions", "subscriptions.status"})
    Optional<UserEntity> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"subscriptions", "subscriptions.status"})
    @Query("SELECT u FROM UserEntity u")
    List<UserEntity> findAll();
}
