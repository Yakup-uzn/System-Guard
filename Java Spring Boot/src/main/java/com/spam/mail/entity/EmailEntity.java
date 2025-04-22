package com.spam.mail.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String messageId;
    private String senderName;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String webLink;

    @Column(columnDefinition = "TEXT")
    private String formLink;

    @Column(columnDefinition = "LONGTEXT")
    private String bodyContent;

    private String alertName;
    private String logSource;
    private String offenseId;
    private String severity;
    private String analyst;
    private int running;
    private String byUser;

    @Column(columnDefinition = "LONGTEXT")
    private String analystComment;

    private String offenseTime;
    private String offenseSource;
    private boolean forward;
    
    
 


    private LocalDateTime createdAt = LocalDateTime.now();
}
