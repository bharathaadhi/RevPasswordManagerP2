package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;
    private final UserRepository userRepository;

    // Get Full Security Report
    @GetMapping("/report/{userId}")
    public ResponseEntity<?> getSecurityReport(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> report = auditService.generateSecurityReport(user);

        return ResponseEntity.ok(report);
    }

    // Get Weak Passwords
    @GetMapping("/weak/{userId}")
    public ResponseEntity<?> getWeakPasswords(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(auditService.getWeakPasswords(user));
    }

    // Get Reused Passwords
    @GetMapping("/reused/{userId}")
    public ResponseEntity<?> getReusedPasswords(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(auditService.getReusedPasswords(user));
    }
}
