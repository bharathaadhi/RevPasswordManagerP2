package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.exception.BadRequestException;
import com.rev.revpasswordmanagerp2.model.SecurityQuestion;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.PasswordEntryRepository;
import com.rev.revpasswordmanagerp2.repository.SecurityQuestionRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.security.JwtUtil;
import com.rev.revpasswordmanagerp2.util.EncryptionUtil;

import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger logger =
            LogManager.getLogger(AuthServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;
    private final SecurityQuestionRepository securityQuestionRepository;
    private final PasswordEntryRepository passwordEntryRepository;
    private final JwtUtil jwtUtil;
    private final VerificationService verificationService;

    /* ================= REGISTER ================= */

    @Override
    @Transactional
    public String registerUser(RegisterRequest request) {

        logger.info("Registration attempt for username: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername()))
            throw new BadRequestException("Username already exists");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new BadRequestException("Email already exists");

        if (request.getSecurityQuestions() == null ||
                request.getSecurityQuestions().size() < 3)
            throw new BadRequestException("Minimum 3 security questions required");

        String encodedPassword =
                passwordEncoder.encode(request.getMasterPassword());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setTwoFactorEnabled(false);
        user.setPassword(encodedPassword);
        user.setMasterPasswordHash(encodedPassword);

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

    /* ================= LOGIN ================= */

    @Override
    public LoginResponseDTO login(LoginRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getMasterPassword(),
                user.getMasterPasswordHash())) {

            throw new RuntimeException("Invalid credentials");
        }

        /* ========= 2FA CHECK ========= */

        if (Boolean.TRUE.equals(user.getTwoFactorEnabled())) {

            VerificationResponseDTO response =
                    verificationService.generateCode(
                            user.getUsername());

            return LoginResponseDTO.builder()
                    .twoFactorRequired(true)
                    .username(user.getUsername())
                    .code(response.getCode())
                    .build();
        }

        /* ========= NORMAL LOGIN ========= */

        String token =
                jwtUtil.generateToken(user.getUsername());

        return LoginResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .userId(user.getId())
                .twoFactorRequired(false)
                .build();
    }

    /* ================= CHANGE PASSWORD ================= */

    @Override
    public String changePassword(ChangePasswordRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getMasterPasswordHash()))
            return "Old password incorrect";

        String encoded =
                passwordEncoder.encode(request.getNewPassword());

        user.setPassword(encoded);
        user.setMasterPasswordHash(encoded);

        userRepository.save(user);

        return "Password changed successfully";
    }

    /* ================= FORGOT PASSWORD ================= */

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<SecurityQuestion> savedQuestions =
                securityQuestionRepository.findByUserId(user.getId());

        boolean verified =
                request.getSecurityQuestions()
                        .stream()
                        .anyMatch(dto ->
                                savedQuestions.stream()
                                        .anyMatch(saved ->
                                                saved.getQuestion()
                                                        .equalsIgnoreCase(dto.getQuestion())
                                                        &&
                                                        passwordEncoder.matches(
                                                                dto.getAnswer(),
                                                                saved.getAnswerHash()
                                                        )));

        if (!verified)
            throw new RuntimeException("Identity verification failed");

        String encodedPassword =
                passwordEncoder.encode(request.getNewPassword());

        user.setPassword(encodedPassword);
        user.setMasterPasswordHash(encodedPassword);

        userRepository.save(user);

        return "Password reset successful";
    }

    /* ================= TOGGLE 2FA ================= */

    @Override
    public String toggleTwoFactor(TwoFactorRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setTwoFactorEnabled(request.getEnable());
        userRepository.save(user);

        return "2FA Updated Successfully";
    }

    /* ================= UPDATE PROFILE ================= */

    @Override
    public String updateProfile(UpdateProfileRequest request){

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setUsername(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        userRepository.save(user);

        return "Profile updated successfully";
    }

    /* ================= CHANGE MASTER PASSWORD ================= */

    @Override
    public String changeMasterPassword(ChangePasswordRequest request){

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if(!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getMasterPasswordHash()))
            return "Current password incorrect";

        String encoded =
                passwordEncoder.encode(request.getNewPassword());

        user.setPassword(encoded);
        user.setMasterPasswordHash(encoded);

        userRepository.save(user);

        return "Master password updated successfully";
    }
}