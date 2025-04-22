package com.spam.mail.controller;

import com.spam.mail.dto.IPInfo;
import com.spam.mail.service.AbuseIPService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ip-check")
public class IPInfoController {

    private final AbuseIPService service;

    public IPInfoController(AbuseIPService service) {
        this.service = service;
    }

    @GetMapping("/{ip}")
    public IPInfo getIPDetails(@PathVariable String ip) {
        return service.checkIP(ip);
    }
}
