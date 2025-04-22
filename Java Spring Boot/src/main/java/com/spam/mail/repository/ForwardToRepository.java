package com.spam.mail.repository;

import com.spam.mail.entity.ForwardToEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForwardToRepository extends JpaRepository<ForwardToEntity, Long> {
}
