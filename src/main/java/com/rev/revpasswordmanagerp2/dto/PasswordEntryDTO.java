package com.rev.revpasswordmanagerp2.dto;

import com.rev.revpasswordmanagerp2.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordEntryDTO {

    private Long id;
    private String accountName;
    private String websiteUrl;
    private String username;

    // Later we will hide password for security
    private String password;

    private Category category;
    private String notes;
    private boolean favorite;

    public PasswordEntryDTO(Long id, String accountName, String websiteUrl, String username, String password, String category, Object notes, boolean favorite) {
    }
}
