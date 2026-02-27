package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.VerificationResponseDTO;

public interface VerificationService {

    VerificationResponseDTO generateCode(String usernameOrEmail);

    boolean validateCode(String usernameOrEmail, String code);
}