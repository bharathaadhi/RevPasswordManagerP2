package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.model.SecurityQuestion;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.SecurityQuestionRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.security.JwtUtil;
import com.rev.revpasswordmanagerp2.service.AuthService;

import com.rev.revpasswordmanagerp2.service.VerificationService;
import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private static final Logger logger =
            LogManager.getLogger(AuthController.class);

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final VerificationService verificationService;
    private final SecurityQuestionRepository securityQuestionRepository;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<VaultResponseDTO> register(
            @RequestBody RegisterRequest request) {

        String message = authService.registerUser(request);

        return ResponseEntity.ok(
                new VaultResponseDTO(message, null)
        );
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElse(null);

        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "User not found"
                    ));
        }

        if (!passwordEncoder.matches(
                request.getMasterPassword(),
                user.getMasterPasswordHash())) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "Invalid password"
                    ));
        }

        /* ===== 2FA ENABLED ===== */

        if (Boolean.TRUE.equals(user.getTwoFactorEnabled())) {

            VerificationResponseDTO otp =
                    verificationService.generateCode(
                            user.getUsername());

            return ResponseEntity.ok(
                    new LoginResponseDTO(
                            null,
                            user.getUsername(),
                            user.getId(),
                            otp.getCode(),
                            true
                    )
            );
        }

        /* ===== NORMAL LOGIN ===== */

        String token =
                jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(
                new LoginResponseDTO(
                        token,
                        user.getUsername(),
                        user.getId(),
                        null,
                        false
                )
        );
    }

    // ================= FORGOT PASSWORD  =================
    @PostMapping("/forgotPassword")
    public ResponseEntity<ApiResponse> forgotPassword(
            @RequestBody ForgotPasswordRequest request){

        logger.warn("Forgot password for user: {}",
                request.getUsernameOrEmail());

        String message =
                authService.forgotPassword(request);

        return ResponseEntity.ok(
                new ApiResponse(message)
        );
    }
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request) {

        return ResponseEntity.ok(
                authService.changePassword(request)
        );
    }

    // ================= SECURITY QUESTIONS =================
    @GetMapping("/security-questions/{usernameOrEmail}")
    public ResponseEntity<List<String>> getQuestions(
            @PathVariable String usernameOrEmail){

        User user = userRepository
                .findByUsernameOrEmail(
                        usernameOrEmail,
                        usernameOrEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<String> questions =
                securityQuestionRepository
                        .findByUserId(user.getId())
                        .stream()
                        .map(SecurityQuestion::getQuestion)
                        .toList();

        return ResponseEntity.ok(questions);
    }

    @PostMapping("/toggle-2fa")
    public ResponseEntity<?> toggle2FA(
            @RequestParam String usernameOrEmail,
            @RequestParam boolean enabled) {

        User user = userRepository
                .findByUsernameOrEmail(
                        usernameOrEmail,
                        usernameOrEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setTwoFactorEnabled(enabled);

        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        enabled
                                ? "2FA Enabled"
                                : "2FA Disabled",
                        "enabled",
                        user.getTwoFactorEnabled()
                )
        );
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2FA(
            @RequestParam String usernameOrEmail,
            @RequestParam String code) {

        boolean valid =
                verificationService.validateCode(
                        usernameOrEmail,
                        code
                );

        if (!valid) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            "Invalid verification code"
                    ));
        }

        User user = userRepository
                .findByUsernameOrEmail(
                        usernameOrEmail,
                        usernameOrEmail)
                .orElseThrow();

        String token =
                jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "username", user.getUsername(),
                        "userId", user.getId()
                )
        );
    }

    @GetMapping("/2fa-status")
    public ResponseEntity<?> get2FAStatus(
            @RequestParam String usernameOrEmail) {

        User user = userRepository
                .findByUsernameOrEmail(
                        usernameOrEmail,
                        usernameOrEmail)
                .orElseThrow();

        return ResponseEntity.ok(
                Map.of(
                        "enabled",
                        user.getTwoFactorEnabled()
                ));
    }
}