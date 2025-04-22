package com.spam.mail.repository;

import com.spam.mail.entity.ReadStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ReadStateRepository extends JpaRepository<ReadStateEntity, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE ReadStateEntity r SET r.lastReadTime = ?2 WHERE r.id = ?1")
    void updateLastReadTime(Long id, String lastReadTime);
}
