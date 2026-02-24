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

    @GetMapping("/report")
    public ResponseEntity<?> getSecurityReport(@RequestParam String usernameOrEmail) {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> report =
                auditService.generateSecurityReport(user);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/weak")
    public ResponseEntity<?> getWeakPasswords(
            @RequestParam String usernameOrEmail) {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PasswordEntryAuditDTO> weakPasswords =
                auditService.getWeakPasswords(user);

        return ResponseEntity.ok(weakPasswords);
    }

    @GetMapping("/reused")
    public ResponseEntity<?> getReusedPasswords(
            @RequestParam String usernameOrEmail) {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PasswordEntryAuditDTO> reusedPasswords =
                auditService.getReusedPasswords(user);

        return ResponseEntity.ok(reusedPasswords);
    }
}
