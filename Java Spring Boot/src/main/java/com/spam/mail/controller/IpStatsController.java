package com.spam.mail.controller;

import com.spam.mail.dto.CountryAttackStatsDTO;
import com.spam.mail.service.IpStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ip-stats")
@RequiredArgsConstructor
public class IpStatsController {

    private final IpStatsService ipStatsService;

    @GetMapping("/blocked")
    public List<CountryAttackStatsDTO> getBlockedIpStats() {
        return ipStatsService.getBlockedIpStatsByCountry();
    }
}
