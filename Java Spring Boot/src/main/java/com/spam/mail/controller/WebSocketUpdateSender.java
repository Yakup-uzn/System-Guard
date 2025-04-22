package com.spam.mail.controller;

import com.spam.mail.dto.ForwardUpdateDTO;
import com.spam.mail.dto.RunningUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketUpdateSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyRunningChange(RunningUpdateDTO dto) {
        messagingTemplate.convertAndSend("/topic/running-updates", dto);
    }
    
    public void notifyForwardChange(ForwardUpdateDTO dto) {
        messagingTemplate.convertAndSend("/topic/forward-updates", dto);
    }
}
