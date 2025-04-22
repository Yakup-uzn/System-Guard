package com.spam.mail.controller;

import com.spam.mail.dto.SolvedRequest;
import com.spam.mail.service.SolvedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solved")
@RequiredArgsConstructor
@Slf4j
public class SolvedController {

    private final SolvedService solvedService;

    @PostMapping
    public ResponseEntity<String> solveForm(@RequestBody SolvedRequest request) {
        boolean success = solvedService.processSolvedRequest(request);
        
        if (!success) {
            return ResponseEntity
                .status(208) // Already Reported
                .body("Bu form daha önce çözümlenmiş olabilir.");
        }

        return ResponseEntity.ok("Form başarıyla gönderildi.");
    }

}
