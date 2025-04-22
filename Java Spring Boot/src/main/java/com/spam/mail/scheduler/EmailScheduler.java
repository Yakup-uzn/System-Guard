package com.spam.mail.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.spam.mail.repository.ProcessedMailRepository;
import com.spam.mail.service.EmailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final EmailService emailService;
    private final ProcessedMailRepository processedMailRepository;
    @Scheduled(fixedRate = 50000) // her 5 dakika
    public void runFetchJob() {
        emailService.fetchAndSaveEmails();
    }
    
    @Scheduled(cron = "0 0 3 * * ?") // her gün saat 03:00
    public void cleanupOldProcessedMails() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(15);
        processedMailRepository.deleteOlderThan(cutoff);
    }
}