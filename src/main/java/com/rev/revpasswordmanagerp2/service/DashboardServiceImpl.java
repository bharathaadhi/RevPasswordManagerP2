package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.DashboardResponse;
import com.rev.revpasswordmanagerp2.dto.PasswordEntryDTO;
import com.rev.revpasswordmanagerp2.model.PasswordEntry;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.PasswordEntryRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.util.PasswordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final PasswordEntryRepository passwordEntryRepository;
    private final AuditService auditService;

    @Override
    public DashboardResponse getDashboard(String usernameOrEmail) {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PasswordEntry> allEntries =
                passwordEntryRepository.findByUser(user);

        long total = allEntries.size();
        long weak = auditService.getWeakPasswords(user).size();
        long reused = auditService.getReusedPasswords(user).size();
        long old = auditService.getOldPasswords(user).size();

        int score = 100
                - (int)(weak * 10)
                - (int)(reused * 15)
                - (int)(old * 5);

        score = Math.max(score, 0);

        String alert = auditService.securityAlert(user);

        List<PasswordEntryDTO> favorites =
                allEntries.stream()
                        .filter(PasswordEntry::getFavorite)
                        .limit(5)
                        .map(PasswordMapper::toDTO)
                        .toList();

        List<PasswordEntryDTO> recent =
                allEntries.stream()
                        .sorted(Comparator.comparing(
                                PasswordEntry::getCreatedAt).reversed())
                        .limit(5)
                        .map(PasswordMapper::toDTO)
                        .toList();

        return new DashboardResponse(
                total,
                weak,
                reused,
                old,
                score,
                alert,
                recent,
                favorites
        );
    }
}