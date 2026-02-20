package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;
import java.util.Map;

@Data
public class UpdateSecurityAnswerRequest {
    private Long userId;
    private String masterPassword;
    private Map<String,String> answers;
}