package com.spam.mail.dto;





import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailMessage {
	private String id;
    private String subject;
    private String bodyPreview;
    private String importance;
    private String createdDateTime;
    private Sender sender;
    private String webLink;
    private Body body;
    private int running;
    private String byUser;
    private boolean forward;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sender {
        private EmailAddress emailAddress;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmailAddress {
        private String name;
        private String address;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        private String contentType;
        private String content;
    }
}

