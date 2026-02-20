package com.rev.revpasswordmanagerp2.service.auth;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.model.SecurityQuestion;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.PasswordEntryRepository;
import com.rev.revpasswordmanagerp2.repository.SecurityQuestionRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SecurityQuestionRepository securityQuestionRepository;
    private final PasswordEntryRepository passwordEntryRepository;
    private final JwtUtil jwtUtil;
    public String registerUser(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (request.getSecurityQuestions() == null
                || request.getSecurityQuestions().size() < 3) {
            throw new RuntimeException("Minimum 3 security questions required");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setTwoFactorEnabled(false);

        user.setMasterPasswordHash(
                passwordEncoder.encode(request.getMasterPassword())
        );

        User savedUser = userRepository.save(user);

        request.getSecurityQuestions().forEach(q -> {
            SecurityQuestion sq = new SecurityQuestion();
            sq.setUser(savedUser);
            sq.setQuestion(q.getQuestion());
            sq.setAnswerHash(passwordEncoder.encode(q.getAnswer()));
            securityQuestionRepository.save(sq);
        });

        return "User Registered Successfully";
    }
    public String login(LoginRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getMasterPassword(),
                user.getMasterPasswordHash())) {

            return "Invalid password";
        }

        return jwtUtil.generateToken(user.getUsername());
    }
    public String changePassword(ChangePasswordRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
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
    public String forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SecurityQuestion> savedQuestions =
                securityQuestionRepository.findByUserId(user.getId());

        if (savedQuestions.size() < 3) {
            throw new RuntimeException("Security questions not configured");
        }

        for (SecurityQuestionDTO dto : request.getSecurityQuestions()) {

            boolean match = savedQuestions.stream().anyMatch(q ->
                    q.getQuestion().equals(dto.getQuestion())
                            &&
                            passwordEncoder.matches(
                                    dto.getAnswer(),
                                    q.getAnswerHash()
                            )
            );

            if (!match) {
                throw new RuntimeException("Security answer mismatch");
            }
        }

        user.setMasterPasswordHash(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        return "Password reset successful";
    }
    public String toggleTwoFactor(TwoFactorRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setTwoFactorEnabled(request.getEnable());
        userRepository.save(user);

        return "2FA Updated Successfully";
    }
    public DashboardResponse getDashboardSummary(String usernameOrEmail){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long totalPasswords =
                passwordEntryRepository.countByUserId(user.getId());

        return new DashboardResponse(
                totalPasswords,
                0,
                "Dashboard Loaded"
        );
    }
}
