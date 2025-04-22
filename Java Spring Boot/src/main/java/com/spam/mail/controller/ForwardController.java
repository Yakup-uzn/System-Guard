package com.spam.mail.controller;

import com.spam.mail.entity.ForwardCcEntity;
import com.spam.mail.entity.ForwardToEntity;
import com.spam.mail.repository.ForwardCcRepository;
import com.spam.mail.repository.ForwardToRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forward")
@RequiredArgsConstructor
public class ForwardController {

    private final ForwardCcRepository ccRepository;
    private final ForwardToRepository toRepository;

    // --- CC KISMI ---

    @GetMapping("/cc")
    public List<ForwardCcEntity> getAllCc() {
        return ccRepository.findAll();
    }

    @PostMapping("/cc")
    public ForwardCcEntity createCc(@RequestBody ForwardCcEntity cc) {
        return ccRepository.save(cc);
    }

    @PutMapping("/cc/{id}")
    public ResponseEntity<?> updateCc(@PathVariable Long id, @RequestBody ForwardCcEntity updated) {
        return ccRepository.findById(id)
                .map(existing -> {
                    existing.setEmail(updated.getEmail());
                    return ResponseEntity.ok(ccRepository.save(existing));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/cc/{id}")
    public ResponseEntity<?> deleteCc(@PathVariable Long id) {
        if (ccRepository.existsById(id)) {
            ccRepository.deleteById(id);
            return ResponseEntity.ok("CC silindi.");
        }
        return ResponseEntity.notFound().build();
    }

    // --- TO KISMI ---

    @GetMapping("/to")
    public List<ForwardToEntity> getAllTo() {
        return toRepository.findAll();
    }

    @PostMapping("/to")
    public ForwardToEntity createTo(@RequestBody ForwardToEntity to) {
        return toRepository.save(to);
    }

    @PutMapping("/to/{id}")
    public ResponseEntity<?> updateTo(@PathVariable Long id, @RequestBody ForwardToEntity updated) {
        return toRepository.findById(id)
                .map(existing -> {
                    existing.setEmail(updated.getEmail());
                    return ResponseEntity.ok(toRepository.save(existing));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/to/{id}")
    public ResponseEntity<?> deleteTo(@PathVariable Long id) {
        if (toRepository.existsById(id)) {
            toRepository.deleteById(id);
            return ResponseEntity.ok("TO silindi.");
        }
        return ResponseEntity.notFound().build();
    }
}
