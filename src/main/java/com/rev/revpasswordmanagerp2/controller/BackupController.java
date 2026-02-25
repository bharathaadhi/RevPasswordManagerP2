package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.BackupEntryDTO;
import com.rev.revpasswordmanagerp2.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backup")
@RequiredArgsConstructor
public class BackupController {

    private final BackupService backupService;

    @GetMapping("/export")
    public List<BackupEntryDTO> exportVault(
            @RequestParam String usernameOrEmail) {

        return backupService.exportVault(usernameOrEmail);
    }
}