package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.*;

public interface AuthService {

    String registerUser(RegisterRequest request);

    String login(LoginRequest request);

    String changePassword(ChangePasswordRequest request);

    String forgotPassword(ForgotPasswordRequest request);

    String toggleTwoFactor(TwoFactorRequest request);

    String updateProfile(UpdateProfileRequest request);

    String changeMasterPassword(ChangePasswordRequest request);
}