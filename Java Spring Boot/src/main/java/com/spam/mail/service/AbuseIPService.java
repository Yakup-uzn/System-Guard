package com.spam.mail.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spam.mail.dto.IPInfo;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AbuseIPService {

    private final String API_KEY = "**************";

    public IPInfo checkIP(String ip) {
        String url = "https://api.abuseipdb.com/api/v2/check?ipAddress=" + ip + "&maxAgeInDays=90";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Key", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.get("data");

            return new IPInfo(
                data.get("abuseConfidenceScore").asInt(),
                data.get("countryCode").asText(),
                data.get("usageType").asText(),
                data.get("isp").asText(),
                data.get("domain").asText()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
