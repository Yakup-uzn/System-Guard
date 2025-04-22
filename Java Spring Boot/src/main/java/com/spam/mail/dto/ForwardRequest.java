package com.spam.mail.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ForwardRequest {
    private String messageId;
    private List<String> to;
    private List<String> cc;
    private String subject;
    private String comment; // mail içeriğine ek açıklama (isteğe bağlı)
}
