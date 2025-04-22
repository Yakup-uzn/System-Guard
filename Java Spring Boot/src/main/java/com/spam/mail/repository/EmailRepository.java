package com.spam.mail.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.spam.mail.entity.EmailEntity;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
    
    boolean existsByMessageId(String messageId);

    List<EmailEntity> findByMessageId(String messageId);

    List<EmailEntity> findBySubject(String subject);

    List<EmailEntity> findByRunningAndByUser(int running, String byUser);

    @Modifying
    @Transactional
    @Query("DELETE FROM EmailEntity e WHERE e.messageId = :messageId")
    void deleteByMessageId(@Param("messageId") String messageId);

    @Query("SELECT e.alertName, COUNT(e) " +
           "FROM EmailEntity e " +
           "WHERE e.alertName IS NOT NULL " +
           "GROUP BY e.alertName")
    List<Object[]> getAlertNameCounts();
}
