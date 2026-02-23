package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.ChangePasswordRequest;
import com.rev.revpasswordmanagerp2.model.VerificationCode;
import com.rev.revpasswordmanagerp2.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @PostMapping("/generate")
    public String generatePassword(
            @RequestParam int length,
            @RequestParam boolean upper,
            @RequestParam boolean lower,
            @RequestParam boolean number,
            @RequestParam boolean special,
            @RequestParam boolean excludeSimilar
    ) {
        return securityService.generatePassword(length, upper, lower, number, special, excludeSimilar);
    }

    @PostMapping("/generate-multiple")
    public List<String> generateMultiple(
            @RequestParam int count,
            @RequestParam int length,
            @RequestParam boolean upper,
            @RequestParam boolean lower,
            @RequestParam boolean number,
            @RequestParam boolean special,
            @RequestParam boolean excludeSimilar
    ) {
        return securityService.generateMultiplePasswords(count, length, upper, lower, number, special, excludeSimilar);
    }

    @PostMapping("/strength")
    public String strength(@RequestParam String password) {
        return securityService.checkStrength(password);
    }

    @PostMapping("/generate-code")
    public VerificationCode generateCode(@RequestParam Long userId) {
        return securityService.generateVerificationCode(userId);
    }

    @PostMapping("/validate-code")
    public boolean validateCode(@RequestParam String code) {
        return securityService.validateCode(code);
    }

    @PostMapping("/validate-master")
    public boolean validateMaster(
            @RequestParam String rawPassword,
            @RequestParam String storedPassword
    ) {
        return securityService.validateMasterPassword(rawPassword, storedPassword);
    }

    @PostMapping("/change-master")
    public String changeMaster(
            @RequestBody ChangePasswordRequest request) {
        return securityService.changeMasterPassword(request);
    }

    @PostMapping("/toggle-2fa")
    public String toggle2FA(
            @RequestParam String usernameOrEmail,
            @RequestParam boolean enabled) {
        return securityService.toggleTwoFactor(usernameOrEmail, enabled);
    }
}
