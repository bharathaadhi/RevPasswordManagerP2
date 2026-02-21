package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String usernameOrEmail;
    private String name;
    private String email;
    private String phone;
}