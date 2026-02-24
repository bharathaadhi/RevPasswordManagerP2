package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.PasswordEntryAuditDTO;
import com.rev.revpasswordmanagerp2.model.PasswordEntry;
import com.rev.revpasswordmanagerp2.model.PasswordHistory;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.PasswordHistoryRepository;
import com.rev.revpasswordmanagerp2.repository.PasswordEntryRepository;
import com.rev.revpasswordmanagerp2.util.EncryptionUtil;
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
    private final EncryptionUtil encryptionUtil;


    // Detect weak passwords
    public List<PasswordEntryAuditDTO> getWeakPasswords(User user) {

        List<PasswordEntry> entries =
                passwordEntryRepository.findByUser(user);

        return entries.stream()
                .filter(entry -> {
                    String decrypted =
                            encryptionUtil.decrypt(entry.getEncryptedPassword());

                    return PasswordStrengthUtil
                            .checkStrength(decrypted)
                            .equalsIgnoreCase("Weak");
                })
                .map(entry -> new PasswordEntryAuditDTO(
                        entry.getId(),
                        entry.getAccountName(),
                        entry.getEncryptedPassword(),
                        entry.getCategory().toString()
                ))
                .collect(Collectors.toList());
    }

    // Detect reused passwords
    public List<PasswordEntryAuditDTO> getReusedPasswords(User user) {

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
                .map(entry -> new PasswordEntryAuditDTO(
                        entry.getId(),
                        entry.getAccountName(),
                        entry.getEncryptedPassword(),
                        entry.getCategory().toString()
                ))
                .collect(Collectors.toList());
    }

    // Generate audit summary
    public Map<String, Object> generateSecurityReport(User user) {

        List<PasswordEntry> all =
                passwordEntryRepository.findByUser(user);

        List<PasswordEntryAuditDTO> weak =
                getWeakPasswords(user);

        List<PasswordEntryAuditDTO> reused =
                getReusedPasswords(user);

        int score = 100
                - (weak.size() * 10)
                - (reused.size() * 15);

        score = Math.max(score, 0);

        String alert;
        if (weak.size() > 0 || reused.size() > 0) {
            alert = "Security Alert: Weak or reused passwords detected";
        } else {
            alert = "All passwords are secure";
        }

        Map<String, Object> report = new HashMap<>();

        report.put("totalPasswords", all.size());
        report.put("weakPasswords", weak.size());
        report.put("reusedPasswords", reused.size());
        report.put("securityScore", score);
        report.put("alertMessage", alert);
        report.put("generatedAt", LocalDateTime.now());

        return report;
    }
    public String securityAlert(User user){

        int weak = getWeakPasswords(user).size();
        int reused = getReusedPasswords(user).size();

        if(weak > 0 || reused > 0){
            return "Security Alert: Weak or reused passwords detected";
        }

        return "All passwords are secure";
    }
}