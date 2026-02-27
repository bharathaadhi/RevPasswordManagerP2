package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.exception.BadRequestException;
import com.rev.revpasswordmanagerp2.model.PasswordEntry;
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

    /* ================= REGISTER ================= */

    @Override
    @Transactional
    public String registerUser(RegisterRequest request) {

        logger.info("Registration attempt for username: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            logger.warn("Registration failed - username already exists: {}", request.getUsername());
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("Registration failed - email already exists: {}", request.getEmail());
            throw new BadRequestException("Email already exists");
        }

        if (request.getSecurityQuestions() == null ||
                request.getSecurityQuestions().size() < 3) {

            logger.error("Registration failed - insufficient security questions");
            throw new BadRequestException("Minimum 3 security questions required");
        }

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

        logger.info("User successfully saved with ID: {}", savedUser.getId());

        request.getSecurityQuestions().forEach(q -> {
            SecurityQuestion sq = new SecurityQuestion();
            sq.setUser(savedUser);
            sq.setQuestion(q.getQuestion());
            sq.setAnswerHash(passwordEncoder.encode(q.getAnswer()));
            securityQuestionRepository.save(sq);
        });

        logger.info("Security questions saved successfully for user: {}", savedUser.getUsername());

        return "User Registered Successfully";
    }

    /* ================= LOGIN ================= */

    @Override
    public LoginResponseDTO login(LoginRequest request) {

        logger.info("Login attempt for: {}", request.getUsernameOrEmail());

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> {
                    logger.warn("Login failed - user not found: {}", request.getUsernameOrEmail());
                    return new RuntimeException("User not found");
                });

        if (!passwordEncoder.matches(
                request.getMasterPassword(),
                user.getMasterPasswordHash())) {

            logger.warn("Login failed - invalid credentials for user: {}", user.getUsername());
            throw new BadRequestException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        logger.info("Login successful for user: {}", user.getUsername());

        return new LoginResponseDTO(
                token,
                user.getUsername(),
                user.getId()
        );
    }

    /* ================= CHANGE PASSWORD ================= */

    @Override
    public String changePassword(ChangePasswordRequest request) {

        logger.info("Change password request for user: {}", request.getUsernameOrEmail());

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> {
                    logger.warn("Change password failed - user not found: {}", request.getUsernameOrEmail());
                    return new RuntimeException("User not found");
                });

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getMasterPasswordHash())) {

            logger.warn("Change password failed - incorrect current password for user: {}", user.getUsername());
            return "Old password incorrect";
        }

        String encoded = passwordEncoder.encode(request.getNewPassword());

        user.setPassword(encoded);
        user.setMasterPasswordHash(encoded);

        userRepository.save(user);

        logger.info("Password changed successfully for user: {}", user.getUsername());

        return "Password changed successfully";
    }

    /* ================= FORGOT PASSWORD ================= */

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {

        logger.warn("Forgot password process initiated for user: {}",
                request.getUsernameOrEmail());

        // ================= FIND USER =================
        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> {

                    logger.error("Forgot password FAILED - User not found: {}",
                            request.getUsernameOrEmail());

                    return new RuntimeException("User not found");
                });

        logger.info("User located successfully: {}", user.getUsername());


        // ================= LOAD SAVED QUESTIONS =================
        List<SecurityQuestion> savedQuestions =
                securityQuestionRepository.findByUserId(user.getId());

        if (savedQuestions == null || savedQuestions.isEmpty()) {

            logger.error("Security questions NOT configured for user: {}",
                    user.getUsername());

            throw new RuntimeException("Security questions not configured");
        }

        logger.info("Loaded {} security questions for user: {}",
                savedQuestions.size(),
                user.getUsername());


        // ================= VALIDATE ANY ONE ANSWER =================
        boolean verified =
                request.getSecurityQuestions()
                        .stream()
                        .anyMatch(dto -> {

                            logger.info("Checking question: {}", dto.getQuestion());

                            return savedQuestions.stream()
                                    .anyMatch(saved -> {

                                        boolean questionMatch =
                                                saved.getQuestion()
                                                        .equalsIgnoreCase(dto.getQuestion());

                                        boolean answerMatch =
                                                passwordEncoder.matches(
                                                        dto.getAnswer(),
                                                        saved.getAnswerHash());

                                        if (questionMatch && answerMatch) {
                                            logger.info(
                                                    "Security answer MATCHED for user: {}",
                                                    user.getUsername());
                                        }

                                        return questionMatch && answerMatch;
                                    });
                        });


        // ================= VERIFICATION RESULT =================
        if (!verified) {

            logger.warn("Security verification FAILED for user: {}",
                    user.getUsername());

            throw new RuntimeException("Identity verification failed");
        }

        logger.info("Identity verification SUCCESS for user: {}",
                user.getUsername());


        // ================= UPDATE PASSWORD =================
        String encodedPassword =
                passwordEncoder.encode(request.getNewPassword());

        user.setPassword(encodedPassword);
        user.setMasterPasswordHash(encodedPassword);

        userRepository.save(user);

        logger.info("Password reset SUCCESSFUL for user: {}",
                user.getUsername());

        return "Password reset successful";
    }


    /* ================= TOGGLE 2FA ================= */

    @Override
    public String toggleTwoFactor(TwoFactorRequest request) {

        logger.warn("2FA toggle request for user: {}", request.getUsernameOrEmail());

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> {
                    logger.warn("2FA toggle failed - user not found: {}", request.getUsernameOrEmail());
                    return new RuntimeException("User not found");
                });

        user.setTwoFactorEnabled(request.getEnable());
        userRepository.save(user);

        logger.info("2FA updated successfully for user: {}", user.getUsername());

        return "2FA Updated Successfully";
    }

    /* ================= UPDATE PROFILE ================= */

    @Override
    public String updateProfile(UpdateProfileRequest request){

        logger.info("Profile update request for userId: {}", request.getUserId());

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> {
                    logger.warn("User not found: {}", request.getUserId());
                    return new RuntimeException("User not found");
                });

        user.setUsername(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        userRepository.save(user);

        logger.info("Profile updated successfully");

        return "Profile updated successfully";
    }

    /* ================= CHANGE MASTER PASSWORD ================= */

    @Override
    public String changeMasterPassword(ChangePasswordRequest request){

        logger.warn("Master password change request for user: {}", request.getUsernameOrEmail());

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> {
                    logger.warn("Master password change failed - user not found: {}", request.getUsernameOrEmail());
                    return new RuntimeException("User not found");
                });

        if(!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getMasterPasswordHash())){

            logger.warn("Master password change failed - incorrect current password for user: {}",
                    user.getUsername());

            return "Current password incorrect";
        }

        String encoded =
                passwordEncoder.encode(request.getNewPassword());

        user.setPassword(encoded);
        user.setMasterPasswordHash(encoded);

        userRepository.save(user);

        logger.info("Master password updated successfully for user: {}", user.getUsername());

        return "Master password updated successfully";
    }
}