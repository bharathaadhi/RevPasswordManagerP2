package com.rev.revpasswordmanagerp2.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationResponseDTO {

    private String message;
    private String email;
    private String code;
}