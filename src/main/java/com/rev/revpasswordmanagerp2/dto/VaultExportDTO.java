package com.rev.revpasswordmanagerp2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaultExportDTO {

    private String accountName;
    private String website;
    private String username;
    private String password;
    private String category;
    private String notes;
}