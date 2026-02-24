package com.rev.revpasswordmanagerp2.dto;

import java.util.List;

public record DashboardResponse(
        long totalPasswords,
        long weakPasswords,
        long reusedPasswords,
        int securityScore,
        String alertMessage,
        List<PasswordEntryDTO> recentEntries,
        List<PasswordEntryDTO> favoritePasswords
) {}
