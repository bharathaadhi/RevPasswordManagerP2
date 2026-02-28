package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;

@Data
public class ExportVaultRequest {

    private String usernameOrEmail;
    private String masterPassword;
    private String verificationCode;
}