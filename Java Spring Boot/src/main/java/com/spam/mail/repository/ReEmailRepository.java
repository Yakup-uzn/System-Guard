package com.spam.mail.repository;

import com.spam.mail.entity.ReEmailEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReEmailRepository extends JpaRepository<ReEmailEntity, Long> {
    
    boolean existsByMessageId(String messageId);
    @Modifying
    @Transactional
    @Query("DELETE FROM ReEmailEntity r WHERE r.messageId = :messageId")
    void deleteByMessageId(@Param("messageId") String messageId);

    Optional<ReEmailEntity> findByRemessageId(String remessageId);
}
