package com.rev.revpasswordmanagerp2.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewPasswordResponseDTO {

    private Long id;
    private String decryptedPassword;
}