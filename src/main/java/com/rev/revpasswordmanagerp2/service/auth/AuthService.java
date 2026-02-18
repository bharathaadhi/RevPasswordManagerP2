package com.rev.revpasswordmanagerp2.service.auth;

import com.rev.revpasswordmanagerp2.dto.ChangePasswordRequest;
import com.rev.revpasswordmanagerp2.dto.ForgotPasswordRequest;
import com.rev.revpasswordmanagerp2.dto.LoginRequest;
import com.rev.revpasswordmanagerp2.dto.RegisterRequest;
import com.rev.revpasswordmanagerp2.model.SecurityQuestion;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.SecurityQuestionRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.rev.revpasswordmanagerp2.security.JwtUtil;


import java.util.List;

@Service
public class AuthService {
    private final JwtUtil jwtutil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SecurityQuestionRepository securityQuestionRepository;

    @Autowired
    public AuthService(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            SecurityQuestionRepository securityQuestionRepository,
            JwtUtil jwtUtil,  JwtUtil jwtutil1
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.securityQuestionRepository = securityQuestionRepository;
        this.jwtutil = jwtutil1;

    }


    // =====================================================
    // ✅ REGISTER USER
    // =====================================================
    public String registerUser(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // ✅ VERY IMPORTANT — HASH PASSWORD
        String hashedPassword = passwordEncoder.encode(request.getMasterPassword());
        user.setMasterPasswordHash(hashedPassword);

        user.setTwoFactorEnabled(false);

        userRepository.save(user);

        return "User Registered Successfully";
    }


    // =====================================================
    // ✅ LOGIN USER
    // =====================================================
    public String login(LoginRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail()
                )
                .orElseThrow(() -> new RuntimeException("User not found"));

        // check password
        if (!passwordEncoder.matches(
                request.getMasterPassword(),
                user.getMasterPasswordHash())) {

            return "Invalid password";
        }

        // 🔥 Generate JWT token
        JwtUtil jwtUtil = null;
        String token = jwtUtil.generateToken(user.getUsername());

        return token;
    }


    // =====================================================
    // ✅ CHANGE PASSWORD
    // =====================================================
    public String changePassword(ChangePasswordRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail()
                )
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getMasterPasswordHash())) {

            return "Old password incorrect";
        }

        user.setMasterPasswordHash(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        return "Password changed successfully";
    }

    // =====================================================
    // ✅ FORGOT PASSWORD
    // =====================================================
    public String forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail()
                )
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SecurityQuestion> savedQuestions = user.getSecurityQuestions();

        if (savedQuestions == null || savedQuestions.isEmpty()) {
            return "No security questions found";
        }

        for (ForgotPasswordRequest.SecurityAnswer answer : request.getAnswers()) {

            boolean match = savedQuestions.stream().anyMatch(q ->
                    q.getQuestion().equalsIgnoreCase(answer.getQuestion()) &&
                            q.getAnswer().equalsIgnoreCase(answer.getAnswer())
            );

            if (!match) {
                return "Security answers do not match";
            }
        }

        user.setMasterPasswordHash(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        return "Password reset successful";
    }
}
