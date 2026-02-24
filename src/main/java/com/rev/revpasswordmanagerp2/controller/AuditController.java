package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.PasswordEntryAuditDTO;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;
    private final UserRepository userRepository;

    @GetMapping("/report/{userId}")
    public ResponseEntity<?> getSecurityReport(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> report = auditService.generateSecurityReport(user);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/weak")
    public ResponseEntity<?> getWeakPasswords(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<PasswordEntryAuditDTO> weakPasswords = auditService.getWeakPasswords(user);
        if (weakPasswords.isEmpty()) {
            return ResponseEntity.ok(
                    Map.of(
                            "message", "No weak passwords found",
                            "data", weakPasswords
                    )
            );
        }
        return ResponseEntity.ok(
                Map.of(
                        "message", "Weak passwords found",
                        "data", weakPasswords
                )
        );
    }

    @GetMapping("/reused")
    public ResponseEntity<?> getReusedPasswords(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<PasswordEntryAuditDTO> reusedPasswords =
                auditService.getReusedPasswords(user);
        if (reusedPasswords.isEmpty()) {
            return ResponseEntity.ok(
                    Map.of(
                            "message", "No reused passwords found",
                            "data", reusedPasswords
                    )
            );
        }
        return ResponseEntity.ok(
                Map.of(
                        "message", "Reused passwords found",
                        "data", reusedPasswords
                )
        );
    }
}
