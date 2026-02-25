package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.ChangePasswordRequest;
import com.rev.revpasswordmanagerp2.model.VerificationCode;
import com.rev.revpasswordmanagerp2.service.SecurityService;
import com.rev.revpasswordmanagerp2.service.VerificationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/security")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;
    private final VerificationService verificationService;


    @PostMapping("/generate")
    public String generatePassword(@RequestBody GeneratorRequest req) {

        return securityService.generatePassword(
                req.getLength(),
                req.isUpper(),
                req.isLower(),
                req.isNumber(),
                req.isSpecial(),
                req.isExcludeSimilar()
        );
    }

    @PostMapping("/generate-multiple")
    public List<String> generateMultiple(@RequestBody MultipleRequest req) {

        return securityService.generateMultiplePasswords(
                req.getCount(),
                req.getLength(),
                req.isUpper(),
                req.isLower(),
                req.isNumber(),
                req.isSpecial(),
                req.isExcludeSimilar()
        );
    }

    @PostMapping("/strength")
    public String strength(@RequestBody StrengthRequest body) {
        return securityService.checkStrength(body.getPassword());
    }

    @PostMapping("/generate-code")
    public String generateCode(@RequestParam String usernameOrEmail) {
        return verificationService.generateCode(usernameOrEmail);
    }

    @PostMapping("/validate-code")
    public boolean validateCode(@RequestParam String usernameOrEmail,
                                @RequestParam String code) {

        return verificationService.validateCode(usernameOrEmail, code);
    }

    @PostMapping("/validate-master")
    public boolean validateMaster(
            @RequestParam String rawPassword,
            @RequestParam String storedPassword
    ) {
        return securityService.validateMasterPassword(rawPassword, storedPassword);
    }

    @PostMapping("/change-master")
    public String changeMaster(@RequestBody ChangePasswordRequest request) {
        return securityService.changeMasterPassword(request);
    }

    @PostMapping("/toggle-2fa")
    public String toggle2FA(
            @RequestParam String usernameOrEmail,
            @RequestParam boolean enabled) {
        return securityService.toggleTwoFactor(usernameOrEmail, enabled);
    }

    @Data
    public static class GeneratorRequest {
        private int length;
        private boolean upper;
        private boolean lower;
        private boolean number;
        private boolean special;
        private boolean excludeSimilar;
    }

    @Data
    public static class MultipleRequest {
        private int count;
        private int length;
        private boolean upper;
        private boolean lower;
        private boolean number;
        private boolean special;
        private boolean excludeSimilar;
    }

    @Data
    public static class StrengthRequest {
        private String password;
    }
}