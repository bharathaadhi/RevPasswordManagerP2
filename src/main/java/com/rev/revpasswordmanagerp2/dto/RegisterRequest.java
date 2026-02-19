package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;
import java.util.List;

@Data
public class RegisterRequest {

    private String username;
    private String email;
    private String phone;
    private String masterPassword;

    // ✅ MUST be List<SecurityQuestionDTO>
    private List<SecurityQuestionDTO> securityQuestions;
}
