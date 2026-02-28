package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.model.SecurityQuestion;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ================= REGISTER =================
    @Override
    public User register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .masterPasswordHash(
                        passwordEncoder.encode(request.getMasterPassword()))
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    // ================= LOGIN =================
    @Override
    public String login(LoginRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail()
                )
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getMasterPassword(),
                user.getMasterPasswordHash())) {

            throw new RuntimeException("Invalid Master Password!");
        }

        return "Login Successful";
    }

    // ================= UPDATE PROFILE =================
    @Override
    public String updateProfile(UpdateProfileRequest request) {

        if (request.getUserId() == null) {
            throw new RuntimeException("UserId missing");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        userRepository.save(user);

        return "Profile updated successfully";
    }

    // ================= GET PROFILE =================
    @Override
    public UserProfileResponse getProfile(String usernameOrEmail) {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileResponse(
                user.getUsername(),
                user.getEmail(),
                user.getPhone()
        );
    }

    // ================= CHANGE MASTER PASSWORD =================
    @Override
    public String changeMasterPassword(ChangePasswordRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail()
                )
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getMasterPasswordHash())) {

            throw new RuntimeException("Old password incorrect");
        }

        user.setMasterPasswordHash(
                passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return "Master Password Updated Successfully";
    }

    // ================= UPDATE SECURITY QUESTIONS =================
    @Override
    public String updateSecurityQuestions(UpdateSecurityAnswerRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail()
                )
                .orElseThrow(() -> new RuntimeException("User not found"));

        SecurityQuestion question = user.getSecurityQuestions()
                .stream()
                .filter(q -> q.getId().equals(request.getQuestionId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Question not found"));

        question.setAnswer(
                request.getAnswer(),
                passwordEncoder
        );

        userRepository.save(user);

        return "Security Question Updated Successfully";
    }
}