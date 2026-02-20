package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.LoginRequest;
import com.rev.revpasswordmanagerp2.dto.RegisterRequest;
import com.rev.revpasswordmanagerp2.entity.User;
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
                .masterPasswordHash(passwordEncoder.encode(request.getMasterPassword()))
                .build();

        return userRepository.save(user);
    }

    @Override
    public String login(LoginRequest request) {

        User user = (User) userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getMasterPassword(), user.getMasterPasswordHash())) {
            throw new RuntimeException("Invalid Master Password!");
        }

        return "Login Successful ✅";
    }
}
