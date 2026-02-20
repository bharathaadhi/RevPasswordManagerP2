package com.rev.revpasswordmanagerp2.dto;

public class ChangePasswordRequest {

    private String usernameOrEmail;
    private String oldPassword;
    private String newPassword;

    public ChangePasswordRequest() {
    }

    public ChangePasswordRequest(String usernameOrEmail,
                                 String oldPassword,
                                 String newPassword) {
        this.usernameOrEmail = usernameOrEmail;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}