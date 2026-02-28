package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.BackupEntryDTO;

import java.util.List;

public interface BackupService {
    List<BackupEntryDTO> exportVault(String usernameOrEmail);
}