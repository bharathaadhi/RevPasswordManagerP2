package com.rev.revpasswordmanagerp2.dto;

import org.jspecify.annotations.Nullable;

public class ChangePasswordRequest {

    private String usernameOrEmail;
    private String currentPassword;
    private String newPassword;


    public ChangePasswordRequest() {
    }


    public ChangePasswordRequest(String usernameOrEmail,
                                 String currentPassword,
                                 String newPassword) {
        this.usernameOrEmail = usernameOrEmail;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }



    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public @Nullable CharSequence getOldPassword() {
        return null;
    }
}
