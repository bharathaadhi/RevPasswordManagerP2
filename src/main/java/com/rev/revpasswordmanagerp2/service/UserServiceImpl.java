package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.LoginRequest;
import com.rev.revpasswordmanagerp2.dto.RegisterRequest;
import com.rev.revpasswordmanagerp2.dto.ChangePasswordRequest;
import com.rev.revpasswordmanagerp2.dto.UpdateProfileRequest;
import com.rev.revpasswordmanagerp2.dto.UpdateSecurityAnswerRequest;
import com.rev.revpasswordmanagerp2.model.SecurityQuestion;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
    public String updateProfile(UpdateProfileRequest request){

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getUsernameOrEmail());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        userRepository.save(user);

        return "Profile Updated Successfully";
    }

    // ================= CHANGE MASTER PASSWORD =================

    @Override
    public String changeMasterPassword(ChangePasswordRequest request){

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail()
                )
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(
                request.getOldPassword(),
                user.getMasterPasswordHash())){

            throw new RuntimeException("Old password incorrect");
        }

        user.setMasterPasswordHash(
                passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return "Master Password Updated Successfully";
    }

    // ================= UPDATE SECURITY QUESTIONS =================

    @Override
    public String updateSecurityQuestions(UpdateSecurityAnswerRequest request){

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

        question.setAnswer(request.getAnswer());

        userRepository.save(user);

        return "Security Question Updated Successfully";
    }
}