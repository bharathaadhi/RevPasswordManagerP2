package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.ChangePasswordRequest;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.model.VerificationCode;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.repository.VerificationCodeRepository;
import com.rev.revpasswordmanagerp2.util.PasswordStrengthUtil;
import com.rev.revpasswordmanagerp2.util.VerificationCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private SecureRandom random = new SecureRandom();

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUM = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+";

    private static final String SIMILAR = "O0l1I";

    // PASSWORD GENERATE
    @Override
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

    // MULTIPLE PASSWORDS
    @Override
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

    // PASSWORD STRENGTH
    @Override
    public String checkStrength(String password) {
        return PasswordStrengthUtil.checkStrength(password);
    }

    // VERIFICATION CODE GENERATE
    @Override
    public VerificationCode generateVerificationCode(Long userId) {

        String code = VerificationCodeUtil.generateCode();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUserId(userId);
        verificationCode.setCode(code);
        verificationCode.setCreatedTime(LocalDateTime.now());
        verificationCode.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        verificationCode.setUsed(false);

        return verificationCodeRepository.save(verificationCode);
    }

    // VERIFY CODE + EXPIRY
    @Override
    public boolean validateCode(String code) {

        VerificationCode vc = verificationCodeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid Code"));

        if (Boolean.TRUE.equals(vc.getUsed()))
            return false;

        if (LocalDateTime.now().isAfter(vc.getExpiryTime()))
            return false;

        vc.setUsed(true);
        verificationCodeRepository.save(vc);

        return true;
    }

    // CHANGE MASTER PASSWORD
    @Override
    public String changeMasterPassword(ChangePasswordRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getMasterPasswordHash())) {

            throw new RuntimeException("Current password incorrect");
        }

        String encoded = passwordEncoder.encode(request.getNewPassword());

        user.setMasterPasswordHash(encoded);
        userRepository.save(user);

        return "Master Password Updated Successfully";
    }

    @Override
    public String toggleTwoFactor(String usernameOrEmail, boolean enabled) {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setTwoFactorEnabled(enabled);
        userRepository.save(user);

        return enabled ? "2FA Enabled" : "2FA Disabled";
    }

    // MASTER PASSWORD CHECK
    @Override
    public boolean validateMasterPassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }
}