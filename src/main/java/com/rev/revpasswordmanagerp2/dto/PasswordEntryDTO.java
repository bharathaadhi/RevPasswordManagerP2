package com.rev.revpasswordmanagerp2.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordEntryDTO {

    private Long id;
    private String accountName;
    private String website;
    private String username;
    private String category;
    private boolean favorite;
    private String createdAt;
    private String updatedAt;
}