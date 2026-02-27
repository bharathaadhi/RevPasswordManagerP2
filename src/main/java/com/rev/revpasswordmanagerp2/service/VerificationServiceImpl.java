package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.VerificationResponseDTO;
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
    public VerificationResponseDTO generateCode(String usernameOrEmail) {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String code = VerificationCodeUtil.generateCode();

        VerificationCode vc = VerificationCode.builder()
                .userId(user.getId())
                .code(code)
                .createdTime(LocalDateTime.now())
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build();

        verificationCodeRepository.save(vc);

        return VerificationResponseDTO.builder()
                .message("Verification code sent")
                .email(user.getEmail())
                .code(code)
                .build();
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