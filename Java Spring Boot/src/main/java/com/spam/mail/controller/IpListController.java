package com.spam.mail.controller;

import com.spam.mail.entity.IpAddress;
import com.spam.mail.service.IpAddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ips")

public class IpListController {

    private final IpAddressService ipAddressService;

    public IpListController(IpAddressService ipAddressService) {
        this.ipAddressService = ipAddressService;
    }


    @GetMapping
    public List<IpAddress> getAllIps() {
        return ipAddressService.getAllIps();
    }

    
    @PostMapping("/status")
    public void toggleStatus(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("id");
        System.out.println("Gelen ID: " + id);
        ipAddressService.toggleIpStatusById(id);
    }

    @PatchMapping("/unblock")
    public ResponseEntity<String> unblockIp(@RequestParam String ipAddress) {
        boolean result = ipAddressService.unblockIpByAddress(ipAddress);
        if (result) {
            return ResponseEntity.ok("IP engeli kaldırıldı.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("IP bulunamadı.");
        }
    }

    @PostMapping("/block")
    public ResponseEntity<?> blockIp(@RequestBody Map<String, Object> ipData) {
        try {
            String ip = (String) ipData.get("ipAddress");
            Optional<IpAddress> existing = ipAddressService.findByIpAddress(ip);

            if (existing.isPresent()) {
                IpAddress existingIp = existing.get();
                existingIp.setStatus(false); // IP engelleniyor
                existingIp.setQueryDate(new Date());
                ipAddressService.save(existingIp);
                return ResponseEntity.ok("IP zaten kayıtlıydı, engellenmiş olarak güncellendi.");
            }

            // Yeni IP oluştur ve engelle
            IpAddress ipAddress = new IpAddress();
            ipAddress.setIpAddress(ip);
            ipAddress.setIsp((String) ipData.get("isp"));
            ipAddress.setAbuseScore((Integer) ipData.get("abuseScore"));
            ipAddress.setCountryCode((String) ipData.get("countryCode"));
            ipAddress.setDomain((String) ipData.get("domain"));
            ipAddress.setQueryDate(new Date());
            ipAddress.setStatus(false); // ENGELLENMİŞ

            ipAddressService.save(ipAddress);
            return ResponseEntity.ok("IP başarıyla kaydedildi ve engellendi.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("İşlem başarısız: " + e.getMessage());
        }
    }
}