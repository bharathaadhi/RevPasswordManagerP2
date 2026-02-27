package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.model.SecurityQuestion;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.SecurityQuestionRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.security.JwtUtil;
import com.rev.revpasswordmanagerp2.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private static final Logger logger =
            LogManager.getLogger(AuthController.class);

    @Autowired
    private final AuthService authService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final SecurityQuestionRepository securityQuestionRepository;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<VaultResponseDTO> register(
            @RequestBody RegisterRequest request) {

        logger.info("Register attempt for user: {}", request.getUsername());

        String message = authService.registerUser(request);

        logger.info("User registered successfully: {}", request.getUsername());

        return ResponseEntity.ok(
                new VaultResponseDTO(message, null)
        );
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getMasterPassword(),
                user.getMasterPasswordHash())) {

            throw new RuntimeException("Invalid password");
        }

        String token =
                jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(
                new LoginResponseDTO(
                        token,
                        user.getUsername(),
                        user.getId()
                )
        );
    }

    // ================= CHANGE PASSWORD =================
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request) {

        logger.info("Change password request for user: {}",
                request.getUsernameOrEmail());

        return ResponseEntity.ok(
                authService.changePassword(request));
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        logger.warn("Forgot password for user: {}",
                request.getUsernameOrEmail());

        return ResponseEntity.ok(
                authService.forgotPassword(request));
    }

    // ================= TOGGLE 2FA =================
    @PostMapping("/toggle2fa")
    public ResponseEntity<String> toggle2FA(
            @RequestBody TwoFactorRequest request) {

        logger.warn("2FA toggle for user: {}",
                request.getUsernameOrEmail());

        return ResponseEntity.ok(
                authService.toggleTwoFactor(request));
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public ResponseEntity<String> logout(){

        logger.info("User logout");

        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/security-questions/{usernameOrEmail}")
    public ResponseEntity<List<String>> getQuestions(
            @PathVariable String usernameOrEmail){

        logger.info("Fetching security questions for: {}", usernameOrEmail);

        User user = userRepository
                .findByUsernameOrEmail(
                        usernameOrEmail,
                        usernameOrEmail)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", usernameOrEmail);
                    return new RuntimeException("User not found");
                });

        List<String> questions =
                securityQuestionRepository
                        .findByUserId(user.getId())
                        .stream()
                        .map(SecurityQuestion::getQuestion)
                        .toList();

        logger.info("Security questions fetched successfully");

        return ResponseEntity.ok(questions);
    }
}