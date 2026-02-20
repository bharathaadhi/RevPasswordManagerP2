package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;

@Data
public class ViewPasswordRequest {
    private Long entryId;
    private String masterPassword;
}