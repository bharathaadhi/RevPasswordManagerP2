package com.rev.revpasswordmanagerp2.dto;

public record DashboardResponse(
        long totalPasswords,
        long weakPasswords,
        String message
) {}
