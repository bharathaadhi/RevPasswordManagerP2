package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private Long userId;
    private String name;
    private String email;
    private String phone;
}