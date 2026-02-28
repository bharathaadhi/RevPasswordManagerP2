package com.rev.revpasswordmanagerp2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String username;
    private Long userId;

    private String code;
    private boolean twoFactorRequired;
}