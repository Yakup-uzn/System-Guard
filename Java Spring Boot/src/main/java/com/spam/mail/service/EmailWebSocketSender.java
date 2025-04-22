package com.spam.mail.service;

import com.spam.mail.entity.EmailEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailWebSocketSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastEmailChange(String action, EmailEntity email) {
        EmailChangeMessage message = new EmailChangeMessage(action, email);
        messagingTemplate.convertAndSend("/topic/email-updates", message);
    }

    @Data
    @AllArgsConstructor
    public static class EmailChangeMessage {
        private String action; // "CREATE" veya "DELETE"
        private EmailEntity email;
    }
}
