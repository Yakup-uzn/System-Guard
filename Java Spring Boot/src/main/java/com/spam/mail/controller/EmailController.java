package com.spam.mail.controller;

import com.spam.mail.dto.ForwardUpdateDTO;
import com.spam.mail.dto.RunningUpdateDTO;
import com.spam.mail.entity.EmailEntity;
import com.spam.mail.repository.EmailRepository;
import com.spam.mail.service.EmailWebSocketSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailRepository emailRepository;
    private final EmailWebSocketSender emailWebSocketSender; // 🔄 WebSocket için yeni servis
    private final WebSocketUpdateSender runningWebSocketSender;


    @GetMapping
    public ResponseEntity<List<EmailEntity>> getAllEmails() {
        List<EmailEntity> emails = emailRepository.findAll();
        return ResponseEntity.ok(emails);
    }

    @GetMapping("/get-by-message-id/{messageId}")
    public ResponseEntity<?> getByMessageId(@PathVariable String messageId) {
        List<EmailEntity> emails = emailRepository.findByMessageId(messageId);

        if (!emails.isEmpty()) {
            return ResponseEntity.ok(emails.get(0)); // veya tamamını döndür
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mail bulunamadı.");
        }
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmailById(@PathVariable Long id) {
        Optional<EmailEntity> emailOpt = emailRepository.findById(id);
        if (!emailOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email bulunamadı.");
        }

        EmailEntity deletedEmail = emailOpt.get();
        emailRepository.deleteById(id);

        // 🔔 WebSocket bildirimi: frontend’e DELETE bildirimi gönder
        emailWebSocketSender.broadcastEmailChange("DELETE", deletedEmail);

        return ResponseEntity.ok("Email başarıyla silindi.");
    }




    @PatchMapping("/{id}/running")
    public ResponseEntity<?> updateRunning(
            @PathVariable Long id,
            @RequestParam int running,
            @RequestParam(required = false) String byUser
    ) {
        Optional<EmailEntity> optionalEmail = emailRepository.findById(id);
        if (!optionalEmail.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        EmailEntity email = optionalEmail.get();

        if (running == 1) {
            email.setRunning(1);
            email.setByUser(byUser);
        } else if (running == 0 && (byUser == null || byUser.equals(email.getByUser()))) {
            email.setRunning(0);
            email.setByUser(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu kullanıcı bu maili kapatamaz.");
        }

        updateRunningStatus(email);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/forward")
    public ResponseEntity<?> markAsForwarded(@PathVariable Long id) {
        Optional<EmailEntity> optionalEmail = emailRepository.findById(id);

        if (!optionalEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email bulunamadı.");
        }

        EmailEntity email = optionalEmail.get();
        email.setForward(true);
        emailRepository.save(email);

        // ✅ WebSocket bildirimi: tüm client'lara gönder
        ForwardUpdateDTO dto = new ForwardUpdateDTO(email.getId(), true);
        runningWebSocketSender.notifyForwardChange(dto);
        return ResponseEntity.ok("Forward durumu true olarak güncellendi.");
    }


    @PostMapping("/close-by-user")
    public ResponseEntity<?> closeByUser(@RequestParam String byUser) {
        System.out.println("🟡 Gelen kullanıcı: " + byUser);
        List<EmailEntity> emails = emailRepository.findByRunningAndByUser(1, byUser);

        for (EmailEntity email : emails) {
            email.setRunning(0);
            email.setByUser(null);
            updateRunningStatus(email);
        }

        return ResponseEntity.ok().build();
    }

    private void updateRunningStatus(EmailEntity email) {
        emailRepository.save(email);

        RunningUpdateDTO dto = new RunningUpdateDTO(
                email.getId(),
                email.getRunning(),
                email.getByUser()
        );

        runningWebSocketSender.notifyRunningChange(dto);
    }
}
