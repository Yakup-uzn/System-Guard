package com.spam.mail.service;

import com.spam.mail.dto.ForwardRequest;
import com.spam.mail.entity.ForwardCcEntity;
import com.spam.mail.entity.ForwardToEntity;
import com.spam.mail.repository.ForwardCcRepository;
import com.spam.mail.repository.ForwardToRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ForwardService {

    private final TokenHolder tokenHolder;
    private final ForwardToRepository forwardToRepository;
    private final ForwardCcRepository forwardCcRepository;

    public boolean forwardMail(ForwardRequest request) {
        String token = tokenHolder.getToken();
        String url = "https://graph.microsoft.com/v1.0/users/**************/messages/" + request.getMessageId() + "/forward";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token.replace("Bearer ", ""));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("comment", request.getComment() != null ? request.getComment() : "");
        body.put("subject", "FWD: SOC Olay Bildirimi");

        // TO: Veritabanından çek
        List<ForwardToEntity> toEntities = forwardToRepository.findAll();
        List<Map<String, Object>> toRecipients = new ArrayList<>();
        for (ForwardToEntity entity : toEntities) {
            toRecipients.add(Map.of("emailAddress", Map.of("address", entity.getEmail())));
        }
        body.put("toRecipients", toRecipients);

        // CC: Veritabanından çek
        List<ForwardCcEntity> ccEntities = forwardCcRepository.findAll();
        if (!ccEntities.isEmpty()) {
            List<Map<String, Object>> ccRecipients = new ArrayList<>();
            for (ForwardCcEntity entity : ccEntities) {
                ccRecipients.add(Map.of("emailAddress", Map.of("address", entity.getEmail())));
            }
            body.put("ccRecipients", ccRecipients);
        }

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(url, entity, Void.class);
            return response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
