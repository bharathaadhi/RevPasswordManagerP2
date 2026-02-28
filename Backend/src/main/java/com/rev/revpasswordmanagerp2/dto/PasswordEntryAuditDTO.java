package com.rev.revpasswordmanagerp2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordEntryAuditDTO {

    private Long id;
    private String accountName;
    private String encryptedPassword;
    private String category;
}