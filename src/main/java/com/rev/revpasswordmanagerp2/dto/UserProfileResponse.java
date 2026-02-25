package com.rev.revpasswordmanagerp2.dto;

public record UserProfileResponse(
        String name,
        String email,
        String phone
) {}