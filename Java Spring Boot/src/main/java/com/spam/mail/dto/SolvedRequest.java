package com.spam.mail.dto;


import lombok.Data;

@Data
public class SolvedRequest {
    private String formLink;
    private String caseResult;
    private String evaluationResult;
    private String name; // kullanıcının maili (frontend'den gönderilecek)
}
