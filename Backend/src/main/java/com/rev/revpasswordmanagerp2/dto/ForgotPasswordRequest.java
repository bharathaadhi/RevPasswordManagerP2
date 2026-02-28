package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;
import java.util.List;

@Data
public class ForgotPasswordRequest {

    private String usernameOrEmail;
    private String newPassword;

    private List<SecurityQuestionDTO> securityQuestions;
}
