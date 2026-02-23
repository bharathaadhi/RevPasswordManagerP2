package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.model.VerificationCode;
import com.rev.revpasswordmanagerp2.model.SecurityQuestion;
import com.rev.revpasswordmanagerp2.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    // ================= PASSWORD GENERATOR =================

    @PostMapping("/generate")
    public ResponseEntity<String> generatePassword(
            @RequestParam int length,
            @RequestParam boolean upper,
            @RequestParam boolean lower,
            @RequestParam boolean number,
            @RequestParam boolean special,
            @RequestParam boolean excludeSimilar
    ) {
        return ResponseEntity.ok(
                securityService.generatePassword(length, upper, lower, number, special, excludeSimilar)
        );
    }

    @PostMapping("/generate-multiple")
    public ResponseEntity<List<String>> generateMultiple(
            @RequestParam int count,
            @RequestParam int length,
            @RequestParam boolean upper,
            @RequestParam boolean lower,
            @RequestParam boolean number,
            @RequestParam boolean special,
            @RequestParam boolean excludeSimilar
    ) {
        return ResponseEntity.ok(
                securityService.generateMultiplePasswords(count, length, upper, lower, number, special, excludeSimilar)
        );
    }

    @PostMapping("/strength")
    public ResponseEntity<String> checkStrength(@RequestParam String password) {
        return ResponseEntity.ok(securityService.checkStrength(password));
    }

    // ================= TWO FACTOR AUTH =================

    @PostMapping("/2fa/generate-code")
    public ResponseEntity<VerificationCode> generateCode(@RequestParam Long userId) {
        return ResponseEntity.ok(securityService.generateVerificationCode(userId));
    }

    @PostMapping("/2fa/validate-code")
    public ResponseEntity<Boolean> validateCode(
            @RequestParam Long userId,
            @RequestParam String code
    ) {
        return ResponseEntity.ok(securityService.validateCode(userId, code));
    }

    @PutMapping("/2fa/enable")
    public ResponseEntity<String> enable2FA(@RequestParam Long userId) {
        securityService.enable2FA(userId);
        return ResponseEntity.ok("2FA Enabled");
    }

    @PutMapping("/2fa/disable")
    public ResponseEntity<String> disable2FA(@RequestParam Long userId) {
        securityService.disable2FA(userId);
        return ResponseEntity.ok("2FA Disabled");
    }

    // ================= MASTER PASSWORD VALIDATION =================

    @PostMapping("/validate-master")
    public ResponseEntity<Boolean> validateMaster(
            @RequestParam Long userId,
            @RequestParam String rawPassword
    ) {
        return ResponseEntity.ok(
                securityService.validateMasterPassword(userId, rawPassword)
        );
    }

    // ================= SECURITY QUESTIONS =================

    @PostMapping("/security-questions")
    public ResponseEntity<String> saveSecurityQuestions(
            @RequestBody List<SecurityQuestion> questions
    ) {
        securityService.saveSecurityQuestions(questions);
        return ResponseEntity.ok("Security questions saved");
    }

    // ================= SECURITY ALERTS =================

    @GetMapping("/alerts")
    public ResponseEntity<List<String>> getSecurityAlerts(@RequestParam Long userId) {
        return ResponseEntity.ok(securityService.getSecurityAlerts(userId));
    }
}