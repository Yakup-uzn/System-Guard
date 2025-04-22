package com.spam.mail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "read_state")
@Getter
@Setter
public class ReadStateEntity {

    @Id
    private Long id = 1L;

    @Column(name = "last_read_time")
    private String lastReadTime;
}
