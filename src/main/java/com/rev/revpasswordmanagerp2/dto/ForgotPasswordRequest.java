package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;
import java.util.List;

@Data
public class ForgotPasswordRequest {

    private String usernameOrEmail;
    private String newPassword;
    private List<SecurityAnswer> answers;

    @Data
    public static class SecurityAnswer {
        private String question;
        private String answer;
    }
}
