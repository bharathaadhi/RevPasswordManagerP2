package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.AnswerRequest;
import com.rev.revpasswordmanagerp2.dto.ChangePasswordRequest;
import com.rev.revpasswordmanagerp2.dto.SecurityAnswerDTO;
import com.rev.revpasswordmanagerp2.model.SecurityQuestion;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.SecurityQuestionRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.repository.UserSecurityAnswerRepository;
import com.rev.revpasswordmanagerp2.repository.VerificationCodeRepository;
import com.rev.revpasswordmanagerp2.util.PasswordStrengthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationService verificationService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SecurityQuestionRepository securityQuestionRepository;
    private final UserSecurityAnswerRepository answerRepository;

    private SecureRandom random = new SecureRandom();

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUM = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+";

    private static final String SIMILAR = "O0l1I";


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


    @Override
    public String checkStrength(String password) {
        return PasswordStrengthUtil.checkStrength(password);
    }


    @Override
    public String changeMasterPassword(ChangePasswordRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate current master password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getMasterPasswordHash())) {

            throw new RuntimeException("Current master password is incorrect");
        }

        // Validate verification code
        verificationService.validateCode(
                request.getUsernameOrEmail(),
                request.getVerificationCode()
        );

        // Update master password
        user.setMasterPasswordHash(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        return "Master password changed successfully";
    }

    @Override
    public boolean validateMasterPassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }

    @Override
    public List<SecurityQuestion> getAllQuestions() {
        return securityQuestionRepository.findAll();
    }

    @Override
    public void updateSecurityAnswers(Long userId,
                                      List<AnswerRequest> answers) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (AnswerRequest req : answers) {

            SecurityQuestion question =
                    securityQuestionRepository
                            .findById(req.getQuestionId())
                            .orElseThrow(() ->
                                    new RuntimeException("Question not found"));

            question.setUser(user);

            question.setAnswer(
                    req.getAnswer(),
                    passwordEncoder
            );

            securityQuestionRepository.save(question);
        }
    }
    @Override
    public void updateSecurityAnswersByUsername(
            String usernameOrEmail,
            List<SecurityAnswerDTO> answers) {

        User user = userRepository
                .findByUsernameOrEmail(
                        usernameOrEmail,
                        usernameOrEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        for (SecurityAnswerDTO dto : answers) {

            SecurityQuestion question =
                    securityQuestionRepository
                            .findByUserIdAndQuestion(
                                    user.getId(),
                                    dto.getQuestion()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException("Question not found"));

            question.setUser(user);

            question.setAnswer(
                    dto.getAnswer(),
                    passwordEncoder
            );

            securityQuestionRepository.save(question);
        }
    }
}