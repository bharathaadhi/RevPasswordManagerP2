package com.rev.revpasswordmanagerp2.util;

import com.rev.revpasswordmanagerp2.dto.PasswordEntryDTO;
import com.rev.revpasswordmanagerp2.entity.PasswordEntry;

public class PasswordMapper {

    public static PasswordEntryDTO toDTO(PasswordEntry entry) {

        return new PasswordEntryDTO(
                entry.getId(),
                entry.getAccountName(),
                entry.getWebsiteUrl(),
                entry.getUsername(),

                // ❌ Do NOT expose password
                "********",

                entry.getCategory(),
                entry.getNotes(),
                entry.isFavorite()
        );
    }
}
