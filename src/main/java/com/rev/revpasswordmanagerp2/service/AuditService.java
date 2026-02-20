package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.model.PasswordEntry;
import com.rev.revpasswordmanagerp2.model.PasswordHistory;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.PasswordHistoryRepository;
import com.rev.revpasswordmanagerp2.repository.PasswordEntryRepository;
import com.rev.revpasswordmanagerp2.util.PasswordStrengthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final PasswordEntryRepository passwordEntryRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;

    // Detect weak passwords
    public List<PasswordEntry> getWeakPasswords(User user) {

        List<PasswordEntry> entries =
                passwordEntryRepository.findByUser(user);

        return entries.stream()
                .filter(entry ->
                        PasswordStrengthUtil.checkStrength(entry.getEncryptedPassword())
                                .equalsIgnoreCase("Weak"))
                .collect(Collectors.toList());
    }

    // Detect reused passwords
    public List<PasswordEntry> getReusedPasswords(User user) {

        List<PasswordEntry> entries =
                passwordEntryRepository.findByUser(user);

        Map<String, Long> passwordCount = entries.stream()
                .collect(Collectors.groupingBy(
                        PasswordEntry::getEncryptedPassword,
                        Collectors.counting()
                ));

        return entries.stream()
                .filter(entry ->
                        passwordCount.get(entry.getEncryptedPassword()) > 1)
                .collect(Collectors.toList());
    }

    // Generate audit summary
    public Map<String, Object> generateSecurityReport(User user) {

        List<PasswordEntry> all =
                passwordEntryRepository.findByUser(user);

        List<PasswordEntry> weak =
                getWeakPasswords(user);

        List<PasswordEntry> reused =
                getReusedPasswords(user);

        Map<String, Object> report = new HashMap<>();

        report.put("totalPasswords", all.size());
        report.put("weakPasswords", weak.size());
        report.put("reusedPasswords", reused.size());
        report.put("generatedAt", LocalDateTime.now());

        return report;
    }
}
