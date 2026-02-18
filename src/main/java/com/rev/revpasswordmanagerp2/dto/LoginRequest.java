package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String usernameOrEmail;
    private String masterPassword;
}
