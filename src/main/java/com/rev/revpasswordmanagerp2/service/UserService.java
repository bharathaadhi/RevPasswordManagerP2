package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.model.User;

public interface UserService {

    // ================= AUTH =================

    User register(RegisterRequest request);

    String login(LoginRequest request);

    // ================= PROFILE =================

    String updateProfile(UpdateProfileRequest request);

    String changeMasterPassword(ChangePasswordRequest request);

    String updateSecurityQuestions(UpdateSecurityAnswerRequest request);

    UserProfileResponse getProfile(String usernameOrEmail);
}