package com.rev.revpasswordmanagerp2.util;

import com.rev.revpasswordmanagerp2.dto.PasswordEntryDTO;
import com.rev.revpasswordmanagerp2.model.PasswordEntry;

public class PasswordMapper {

    public static PasswordEntryDTO toDTO(PasswordEntry entry) {

        return PasswordEntryDTO.builder()
                .id(entry.getId())
                .accountName(entry.getAccountName())
                .website(entry.getWebsiteUrl())
                .username(entry.getAccountUsername())
                .category(entry.getCategory().name())
                .favorite(entry.getFavorite())
                .createdAt(entry.getCreatedAt() != null ?
                        entry.getCreatedAt().toString() : null)
                .updatedAt(entry.getUpdatedAt() != null ?
                        entry.getUpdatedAt().toString() : null)
                .build();
    }
}