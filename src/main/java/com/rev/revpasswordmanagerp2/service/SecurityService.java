package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.model.SecurityQuestion;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.model.VerificationCode;
import com.rev.revpasswordmanagerp2.repository.VerificationCodeRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.util.PasswordStrengthUtil;
import com.rev.revpasswordmanagerp2.util.VerificationCodeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SecureRandom random = new SecureRandom();

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUM = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+";
    private static final String SIMILAR = "O0l1I";

    // ================= PASSWORD GENERATOR =================

    public String generatePassword(int length, boolean upper, boolean lower,
                                   boolean number, boolean special, boolean excludeSimilar) {

        if (length < 8 || length > 64) {
            throw new RuntimeException("Length must be 8 to 64");
        }

        String pool = "";

        if (upper) pool += UPPER;
        if (lower) pool += LOWER;
        if (number) pool += NUM;
        if (special) pool += SPECIAL;

        if (excludeSimilar) {
            for (char c : SIMILAR.toCharArray()) {
                pool = pool.replace(String.valueOf(c), "");
            }
        }

        if (pool.isEmpty()) throw new RuntimeException("Select at least one type");

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(pool.charAt(random.nextInt(pool.length())));
        }

        return password.toString();
    }

    public List<String> generateMultiplePasswords(int count, int length,
                                                  boolean upper, boolean lower,
                                                  boolean number, boolean special,
                                                  boolean excludeSimilar) {

        List<String> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            list.add(generatePassword(length, upper, lower, number, special, excludeSimilar));
        }

        return list;
    }

    public String checkStrength(String password) {
        return PasswordStrengthUtil.checkStrength(password);
    }

    // ================= 2FA =================

    public VerificationCode generateVerificationCode(Long userId) {

        String code = VerificationCodeUtil.generateCode();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUserId(userId);
        verificationCode.setCode(code);
        verificationCode.setCreatedTime(LocalDateTime.now());
        verificationCode.setUsed(false);

        return verificationCodeRepository.save(verificationCode);
    }

    public boolean validateCode(Long userId, String code) {

        VerificationCode vc = verificationCodeRepository
                .findByUserIdAndCode(userId, code)
                .orElseThrow(() -> new RuntimeException("Invalid Code"));

        if (Boolean.TRUE.equals(vc.getUsed()))
            return false;

        LocalDateTime expiry = vc.getCreatedTime().plusMinutes(5);

        if (LocalDateTime.now().isAfter(expiry))
            return false;

        vc.setUsed(true);
        verificationCodeRepository.save(vc);

        return true;
    }

    public void enable2FA(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setTwoFactorEnabled(true);
        userRepository.save(user);
    }

    public void disable2FA(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setTwoFactorEnabled(false);
        userRepository.save(user);
    }

    // ================= MASTER PASSWORD =================
    // Using existing password field

    public boolean validateMasterPassword(Long userId, String rawPassword) {

        User user = userRepository.findById(userId).orElseThrow();

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // ================= SECURITY QUESTIONS =================

    public void saveSecurityQuestions(List<SecurityQuestion> questions) {
        // keep empty for now (no repository added yet)
    }

    // ================= SECURITY ALERTS =================

    public List<String> getSecurityAlerts(Long userId) {

        List<String> alerts = new ArrayList<>();

        alerts.add("Weak password detected");
        alerts.add("Multiple login attempts");

        return alerts;
    }
}