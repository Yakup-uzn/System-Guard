package com.spam.mail.repository;

import com.spam.mail.entity.ForwardCcEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForwardCcRepository extends JpaRepository<ForwardCcEntity, Long> {
}
