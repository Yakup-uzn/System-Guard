package com.spam.mail.service;

import com.spam.mail.dto.SolvedRequest;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolvedService {

    private final SolvedFormSubmitter solvedFormSubmitter;

    public boolean processSolvedRequest(SolvedRequest request) {
        String cleanedName = extractAndCleanName(request.getName());
        request.setName(cleanedName);

        return solvedFormSubmitter.submitForm(request);
    }


    public String extractAndCleanName(String email) {
        if (email == null || !email.contains("@")) return "";

        // 🔒 Manuel tanımlı adlar
        Map<String, String> manualNameMap = Map.of(
            "yakup.uzunoglu@gmail.com", "Yakup Uzunoğlu"
        );

        if (manualNameMap.containsKey(email.toLowerCase())) {
            return manualNameMap.get(email.toLowerCase());
        }

        // Otomatik işleme geç
        String beforeAt = email.substring(0, email.indexOf('@'));
        String replaced = beforeAt.replace(".", " ");

        StringBuilder cleanedBuilder = new StringBuilder();
        for (char ch : replaced.toCharArray()) {
            switch (ch) {
                case 'ç': ch = 'c'; break;
                case 'Ç': ch = 'C'; break;
                case 'ğ': ch = 'g'; break;
                case 'Ğ': ch = 'G'; break;
                case 'ş': ch = 's'; break;
                case 'Ş': ch = 'S'; break;
                case 'ü': ch = 'u'; break;
                case 'Ü': ch = 'U'; break;
                case 'ö': ch = 'o'; break;
                case 'Ö': ch = 'O'; break;
                case 'ı': ch = 'i'; break;
                case 'İ': ch = 'I'; break;
            }
            if (Character.isLetter(ch) || Character.isWhitespace(ch)) {
                cleanedBuilder.append(ch);
            }
        }

        String cleaned = cleanedBuilder.toString().trim();

        String[] parts = cleaned.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            result.append(part.substring(0, 1).toUpperCase(new Locale("tr", "TR")))
                  .append(part.substring(1).toLowerCase(new Locale("tr", "TR")))
                  .append(" ");
        }

        return result.toString().trim();
    }




} 