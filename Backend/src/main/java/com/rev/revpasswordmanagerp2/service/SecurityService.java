package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.AnswerRequest;
import com.rev.revpasswordmanagerp2.dto.ChangePasswordRequest;
import com.rev.revpasswordmanagerp2.dto.SecurityAnswerDTO;
import com.rev.revpasswordmanagerp2.model.SecurityQuestion;

import java.util.List;

public interface SecurityService {

    String generatePassword(int length, boolean upper, boolean lower,
                            boolean number, boolean special, boolean excludeSimilar);

    List<String> generateMultiplePasswords(int count, int length,
                                           boolean upper, boolean lower,
                                           boolean number, boolean special,
                                           boolean excludeSimilar);

    String checkStrength(String password);

    String changeMasterPassword(ChangePasswordRequest request);

    boolean validateMasterPassword(String raw, String encoded);

    List<SecurityQuestion> getAllQuestions();

    void updateSecurityAnswers(Long userId,
                               List<AnswerRequest> answers);

    void updateSecurityAnswersByUsername(
            String usernameOrEmail,
            List<SecurityAnswerDTO> answers);
}