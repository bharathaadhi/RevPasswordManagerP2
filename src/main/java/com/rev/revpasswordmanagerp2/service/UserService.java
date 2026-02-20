package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.LoginRequest;
import com.rev.revpasswordmanagerp2.dto.RegisterRequest;
import com.rev.revpasswordmanagerp2.model.User;

public interface UserService {

    User register(RegisterRequest request);

    String login(LoginRequest request);
}