package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;

@Data
public class DeletePasswordRequest {

    private Long entryId;
    private String usernameOrEmail;
    private String masterPassword;
    private String verificationCode;
}