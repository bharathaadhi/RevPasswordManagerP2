package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.model.VerificationCode;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.repository.VerificationCodeRepository;
import com.rev.revpasswordmanagerp2.util.VerificationCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    public String generateCode(String usernameOrEmail) {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String code = VerificationCodeUtil.generateCode();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUserId(user.getId());
        verificationCode.setCode(code);
        verificationCode.setCreatedTime(LocalDateTime.now());
        verificationCode.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        verificationCode.setUsed(false);

        verificationCodeRepository.save(verificationCode);

        // Simulated email
        System.out.println("Verification Code for " + user.getEmail() + ": " + code);

        return "Verification code sent (simulated)";
    }

    @Override
    public boolean validateCode(String usernameOrEmail, String code) {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VerificationCode vc =
                verificationCodeRepository
                        .findTopByUserIdAndUsedFalseOrderByCreatedTimeDesc(user.getId())
                        .orElseThrow(() -> new RuntimeException("No active code found"));

        if (!vc.getCode().equals(code)) {
            throw new RuntimeException("Invalid Code");
        }

        if (LocalDateTime.now().isAfter(vc.getExpiryTime())) {
            throw new RuntimeException("Code Expired");
        }

        vc.setUsed(true);
        verificationCodeRepository.save(vc);

        return true;
    }
}