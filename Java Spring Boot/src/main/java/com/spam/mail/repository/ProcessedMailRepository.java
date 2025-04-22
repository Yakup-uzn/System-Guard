package com.spam.mail.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spam.mail.entity.ProcessedMailEntity;

import jakarta.transaction.Transactional;

public interface ProcessedMailRepository extends JpaRepository<ProcessedMailEntity, String> {
    boolean existsByMessageId(String messageId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProcessedMailEntity p WHERE p.processedAt < :cutoff")
    void deleteOlderThan(@Param("cutoff") LocalDateTime cutoff);
}

