package com.spam.mail.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spam.mail.dto.EmailMessage;
import com.spam.mail.entity.EmailEntity;
import com.spam.mail.entity.ProcessedMailEntity;
import com.spam.mail.entity.ReEmailEntity;
import com.spam.mail.entity.ReadStateEntity;
import com.spam.mail.repository.EmailRepository;
import com.spam.mail.repository.ProcessedMailRepository;
import com.spam.mail.repository.ReEmailRepository;
import com.spam.mail.repository.ReadStateRepository;
import lombok.RequiredArgsConstructor;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Duration;
import java.util.*;
import org.springframework.web.reactive.function.client.WebClient;

//Mail.ReadWrite.All ekle

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;
    private final ReEmailRepository reEmailRepository;
    private final ReadStateRepository readStateRepository;
    private final TokenRefreshService tokenRefreshService;
    private final TokenHolder tokenHolder;
    private final ProcessedMailRepository processedMailRepository;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://graph.microsoft.com/v1.0")
            .defaultHeader("Content-Type", "application/json")
            .build();

    public void fetchAndSaveEmails() {
        try {
            String currentToken = tokenHolder.getToken();
            fetchEmailsWithToken(currentToken);
        } catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("\u2757 Token süre doldu. Yeni token alınıyor...");
            String newToken = tokenRefreshService.refreshAccessToken();
            if (newToken != null) {
                fetchEmailsWithToken(newToken);
            } else {
                System.err.println("Yeni token alınamadı.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchEmailsWithToken(String token) {
        String lastReadTime = getLastReadTime();
        if (lastReadTime != null && !lastReadTime.endsWith("Z")) {
            lastReadTime += "Z";
        }

        StringBuilder urlBuilder = new StringBuilder("https://graph.microsoft.com/v1.0/users/***************/messages");
        List<String> queryParams = new ArrayList<>();

        if (lastReadTime != null) {
            queryParams.add("receivedDateTime gt " + lastReadTime);
        }
        queryParams.add("isRead eq false");

        if (!queryParams.isEmpty()) {
            urlBuilder.append("?$filter=").append(String.join(" and ", queryParams));
            urlBuilder.append("&$orderby=receivedDateTime desc");
            urlBuilder.append("&$select=id,subject,bodyPreview,sender,receivedDateTime,body,webLink");
        }

        String url = urlBuilder.toString();
        System.out.println("\ud83d\udd17 Graph API URL: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("Accept", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        JsonNode messages = response.getBody().get("value");

        ObjectMapper mapper = new ObjectMapper();

        for (JsonNode node : messages) {
            EmailMessage message;
            try {
                message = mapper.treeToValue(node, EmailMessage.class);
            } catch (Exception e) {
                System.err.println("Email verisi çözümlenemedi: " + e.getMessage());
                continue;
            }

            String subject = Optional.ofNullable(message.getSubject()).orElse("").trim();
            String messageId = message.getId();

            if (processedMailRepository.existsByMessageId(messageId)) {
                System.out.println("📭 Bu mail zaten işlendi (DB): " + messageId);
                continue;
            }
            

            if (subject.startsWith("SOC Olay Bildirimi")) {
                handleNewSocAlert(message);
            } else if (subject.startsWith("Re: FW:")) {
                handleReplyMessage(message);
            }
            
            processedMailRepository.save(new ProcessedMailEntity(messageId));
            markEmailAsRead(token, messageId);
        }
    }

    private void markEmailAsRead(String token, String messageId) {
        try {
            webClient.patch()
                    .uri("/users/**********/messages/" + messageId)
                    .header("Authorization", token)
                    .bodyValue(Map.of("isRead", true))
                    .retrieve()
                    .toBodilessEntity()
                    .block((Duration.ofSeconds(10)));

            System.out.println("\u2705 Mail okundu olarak işaretlendi: " + messageId);
        } catch (Exception e) {
            System.err.println("Mail işaretlenemedi: " + e.getMessage());
        }
    }

    private String getLastReadTime() {
        return readStateRepository.findById(1L)
                .map(ReadStateEntity::getLastReadTime)
                .orElse("2024-01-01T00:00:00Z");
    }

    private void handleNewSocAlert(EmailMessage message) {
        EmailEntity email = new EmailEntity();
        email.setMessageId(message.getId());
        email.setSenderName(Optional.ofNullable(message.getSender())
                .map(s -> s.getEmailAddress().getName())
                .orElse("Bilinmiyor"));
        email.setSubject(Optional.ofNullable(message.getSubject()).orElse("Konu Yok"));
        email.setWebLink(Optional.ofNullable(message.getWebLink()).orElse("#"));

        String htmlContent = Optional.ofNullable(message.getBody())
                .map(EmailMessage.Body::getContent)
                .orElse("");
        email.setBodyContent(htmlContent);

        Map<String, String> fields = extractHtmlFields(htmlContent);
        email.setAlertName(fields.get("Alert Name"));
        email.setLogSource(fields.get("Log Source"));
        email.setOffenseId(fields.get("Offense ID"));
        email.setSeverity(fields.get("Severity"));
        email.setAnalyst(fields.get("Analyst"));
        email.setAnalystComment(fields.get("Analyst Comment"));
        email.setOffenseTime(fields.get("Offense Time"));
        email.setOffenseSource(fields.get("Offense Source"));
        email.setFormLink(fields.get("Form Link"));

        emailRepository.save(email);
        System.out.println("📌 Yeni mail eklendi: " + message.getId());
    }

    
    
    
    
    
    private void handleReplyMessage(EmailMessage message) {
        ReEmailEntity email = new ReEmailEntity();

        // 1. Orijinal subject'i al
        String originalSubject = Optional.ofNullable(message.getSubject()).orElse("Konu Yok");

        // 2. "Re: FW: " veya "FW: " gibi prefix'leri temizle
        String cleanedSubject = originalSubject;
        if (cleanedSubject.startsWith("Re: FW: ")) {
            cleanedSubject = cleanedSubject.substring("Re: FW: ".length()).trim();
        } else if (cleanedSubject.startsWith("FW: ")) {
            cleanedSubject = cleanedSubject.substring("FW: ".length()).trim();
        }

        // 3. Subject'e göre EmailEntity'yi bul
        List<EmailEntity> emailList = emailRepository.findBySubject(cleanedSubject);

        System.out.println("🔍 Gelen Subject: " + cleanedSubject);
        System.out.println("🔢 Eşleşen kayıt sayısı: " + emailList.size());

        for (EmailEntity e : emailList) {
            System.out.println("📬 ID: " + e.getId() + " | MessageId: " + e.getMessageId() + " | Subject: " + e.getSubject());
        }

        if (!emailList.isEmpty()) {
            EmailEntity originalEmail = emailList.get(0);
            email.setMessageId(originalEmail.getMessageId()); // Orijinal mailin messageId'si
        } else {
            email.setMessageId(null); // Yedek olarak bu mesajın ID’si
        }

        email.setRemessageId(message.getId());
        email.setSubject(originalSubject); // Kullanıcının gördüğü subject aynen kalsın

        // 4. BodyPreview içinden '<' karakterine kadar al
        String bodyPreview = message.getBodyPreview();
        String cleanBodyPreview = bodyPreview != null && bodyPreview.contains("<")
                ? bodyPreview.substring(0, bodyPreview.indexOf("<"))
                : bodyPreview;

        email.setRequest(cleanBodyPreview);

        // 5. Kayıt et
        reEmailRepository.save(email);
    }


    



    private static final Map<String, String> NORMALIZED_KEYS = Map.ofEntries(
            Map.entry("alert name", "Alert Name"),
            Map.entry("log source", "Log Source"),
            Map.entry("offense id", "Offense ID"),
            Map.entry("offense ıd", "Offense ID"),
            Map.entry("severity", "Severity"),
            Map.entry("analyst", "Analyst"),
            Map.entry("l1 analyst", "Analyst"),
            Map.entry("analyst comment", "Analyst Comment"),
            Map.entry("offense time", "Offense Time"),
            Map.entry("threat occurred time", "Offense Time"),
            Map.entry("offense source", "Offense Source")
    );

    private Map<String, String> extractHtmlFields(String html) {
        Map<String, String> extracted = new LinkedHashMap<>();
        for (String key : NORMALIZED_KEYS.values()) {
            extracted.put(key, null);
        }
        extracted.put("Form Link", null);

        try {
            Document doc = Jsoup.parse(html);
            for (Element link : doc.select("a[href]")) {
                String text = link.text().toLowerCase();
                if (text.contains("anket") || text.contains("tıklayın")) {
                    extracted.put("Form Link", link.attr("href"));
                    break;
                }
            }

            for (Element row : doc.select("tr")) {
                Element keyEl = row.selectFirst(".vertical-left");
                Element valEl = row.selectFirst(".vertical-right");

                if (keyEl != null && valEl != null) {
                    String rawKey = keyEl.text().toLowerCase().replace(":", "").trim();
                    String value = valEl.text().trim();
                    String normalizedKey = NORMALIZED_KEYS.get(rawKey);
                    if (normalizedKey != null) {
                        extracted.put(normalizedKey, value);
                    } else {
                        System.out.println("⚠️ Bilinmeyen alan bulundu: '" + rawKey + "' = " + value);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❗ HTML parsing hatası: " + e.getMessage());
        }

        return extracted;
    }
}

