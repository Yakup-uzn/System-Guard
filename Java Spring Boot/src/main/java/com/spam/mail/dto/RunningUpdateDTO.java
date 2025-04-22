package com.spam.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RunningUpdateDTO {
    private Long emailId;
    private int running;
    private String byUser;
}

