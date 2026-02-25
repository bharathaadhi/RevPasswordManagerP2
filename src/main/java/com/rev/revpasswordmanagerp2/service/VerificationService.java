package com.rev.revpasswordmanagerp2.service;

public interface VerificationService {

    String generateCode(String usernameOrEmail);

    boolean validateCode(String usernameOrEmail, String code);
}