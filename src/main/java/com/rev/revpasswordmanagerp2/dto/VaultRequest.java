package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;

@Data
public class VaultRequest {

    private String usernameOrEmail;
    private String accountName;
    private String website;
    private String username;
    private String password;
    private String category;
}
