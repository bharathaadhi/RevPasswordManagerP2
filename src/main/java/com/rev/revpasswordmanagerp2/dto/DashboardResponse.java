package com.rev.revpasswordmanagerp2.dto;

import java.util.List;

public record DashboardResponse(
        long totalPasswords,
        long weakPasswords,
        String message,
        List<PasswordEntryDTO> recentEntries
) {}
