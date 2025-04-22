package com.spam.mail.controller;

import com.spam.mail.entity.EmailEntity;
import com.spam.mail.entity.ReEmailEntity;
import com.spam.mail.repository.EmailRepository;
import com.spam.mail.repository.ReEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/remails")
@RequiredArgsConstructor
public class ReEmailController {

    private final ReEmailRepository reEmailRepository;
    private final EmailRepository emailRepository;

    @GetMapping
    public List<ReEmailEntity> getAllEmails() {
        return reEmailRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReEmailEntity> getEmailById(@PathVariable Long id) {
        Optional<ReEmailEntity> emailOpt = reEmailRepository.findById(id);
        return emailOpt.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmailById(@PathVariable Long id) {
        if (reEmailRepository.existsById(id)) {
            reEmailRepository.deleteById(id);
            return ResponseEntity.ok("Email başarıyla silindi.");
        } else {
            return ResponseEntity.status(404).body("Email bulunamadı.");
        }
    }

    @DeleteMapping("/delete-by-message-id/{messageId}")
    public ResponseEntity<?> deleteByMessageId(@PathVariable String messageId) {
        boolean deletedAny = false;

        System.out.println("➡️ Silme işlemi başlatıldı. Gelen messageId: " + messageId);

        // 1. ReEmailEntity kontrol ve silme
        if (reEmailRepository.existsByMessageId(messageId)) {
            reEmailRepository.deleteByMessageId(messageId);
            System.out.println("✅ ReEmailEntity silindi (messageId eşleşti).");
            deletedAny = true;
        } else {
            System.out.println("❌ ReEmailEntity bulunamadı (messageId: " + messageId + ").");
        }

        // 2. EmailEntity kontrol ve silme
        if (emailRepository.existsByMessageId(messageId)) {
            emailRepository.deleteByMessageId(messageId);
            System.out.println("✅ EmailEntity silindi (messageId eşleşti).");
            deletedAny = true;
        } else {
            System.out.println("❌ EmailEntity bulunamadı (messageId: " + messageId + ").");
        }

        if (deletedAny) {
            return ResponseEntity.ok("✅ İlgili mail(ler) başarıyla silindi.");
        } else {
            return ResponseEntity.status(404).body("❌ Hiçbir eşleşen mail bulunamadı.");
        }
    }



}
