package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.service.SecurityService;
import com.rev.revpasswordmanagerp2.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/security")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;
    private final VerificationService verificationService;
    private final UserRepository userRepository;


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
    public VerificationResponseDTO generateCode(
            @RequestParam String usernameOrEmail) {

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

    @GetMapping("/questions")
    public ResponseEntity<?> getQuestions() {
        return ResponseEntity.ok(
                securityService.getAllQuestions()
        );
    }

    @PostMapping("/update-answers")
    public ResponseEntity<?> updateAnswers(
            @RequestBody UpdateAnswersRequest request) {

        if (request.getUserId() != null) {

            securityService.updateSecurityAnswers(
                    request.getUserId(),
                    request.getAnswers());

        } else {

            securityService.updateSecurityAnswersByUsername(
                    request.getUsernameOrEmail(),
                    request.getSecurityQuestions());
        }

        return ResponseEntity.ok("Answers updated successfully");
    }
}