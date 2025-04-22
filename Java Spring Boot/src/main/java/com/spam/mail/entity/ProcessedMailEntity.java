package com.spam.mail.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "processed_mails")
@Data
@AllArgsConstructor
public class ProcessedMailEntity {

    @Id
    private String messageId;

    @Column(nullable = false)
    private LocalDateTime processedAt = LocalDateTime.now();
    
    public ProcessedMailEntity() {
        this.processedAt = LocalDateTime.now();
    }

    public ProcessedMailEntity(String messageId) {
        this.messageId = messageId;
        this.processedAt = LocalDateTime.now();
    }
}
