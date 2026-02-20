package com.rev.revpasswordmanagerp2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaultResponseDTO {

    private String message;
    private Object data;
}
