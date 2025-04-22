package com.spam.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlertCountDTO {
    private String alertName;
    private Long count;
}
