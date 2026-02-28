package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.BackupEntryDTO;
import com.rev.revpasswordmanagerp2.model.PasswordEntry;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.PasswordEntryRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {

    private final UserRepository userRepository;
    private final PasswordEntryRepository passwordEntryRepository;

    @Override
    public List<BackupEntryDTO> exportVault(String usernameOrEmail) {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PasswordEntry> entries =
                passwordEntryRepository.findByUser(user);

        return entries.stream()
                .map(entry -> BackupEntryDTO.builder()
                        .id(entry.getId())
                        .accountName(entry.getAccountName())
                        .website(entry.getWebsiteUrl())
                        .username(entry.getAccountUsername())
                        .category(entry.getCategory().name())
                        .favorite(entry.getFavorite())
                        .encryptedPassword(entry.getEncryptedPassword())
                        .createdAt(entry.getCreatedAt().toString())
                        .updatedAt(entry.getUpdatedAt().toString())
                        .build())
                .toList();
    }
}