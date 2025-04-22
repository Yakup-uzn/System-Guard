package com.spam.mail.controller;

import com.spam.mail.dto.AlertCountDTO;
import com.spam.mail.service.AlertStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor

public class AlertStatsController {

    private final AlertStatsService alertStatsService;

    @GetMapping("/counts")
    public List<AlertCountDTO> getAlertCounts() {
        return alertStatsService.getAlertNameStats();
    }
}
