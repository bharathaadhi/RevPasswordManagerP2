package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;
import java.util.List;

@Data
public class ImportRequest {

    private String usernameOrEmail;
    private String masterPassword;
    private String verificationCode;

    private List<PasswordEntryDTO> vaultData;
}