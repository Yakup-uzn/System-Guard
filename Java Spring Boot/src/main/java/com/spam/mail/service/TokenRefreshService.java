package com.spam.mail.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {

    @Value("${azure.client-id}")
    private String clientId;

    @Value("${azure.client-secret}")
    private String clientSecret;

    @Value("${azure.refresh-token}")
    private String refreshToken;

    @Value("${azure.tenant-id}")
    private String tenantId;

    private final TokenHolder tokenHolder;

    public String refreshAccessToken() {
        String url = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("scope", "https://graph.microsoft.com/.default");
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, request, JsonNode.class);
            String newToken = response.getBody().get("access_token").asText();
            String bearerToken = "Bearer " + newToken;

            tokenHolder.setToken(bearerToken); // Bellekte güncelle
            System.out.println("🔁 Yeni token başarıyla alındı.");
            return bearerToken;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
