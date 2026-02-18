package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;
import java.util.List;

@Data
public class RegisterRequest {

    private String username;
    private String email;
    private String masterPassword;
    private String phone;

    private List<SecurityQuestionDTO> securityQuestions;
}
