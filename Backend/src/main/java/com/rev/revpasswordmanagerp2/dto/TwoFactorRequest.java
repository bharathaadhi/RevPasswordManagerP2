package com.rev.revpasswordmanagerp2.dto;

public class TwoFactorRequest {

    private String usernameOrEmail;
    private Boolean enable;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public Boolean getEnable() {
        return enable;
    }
}
