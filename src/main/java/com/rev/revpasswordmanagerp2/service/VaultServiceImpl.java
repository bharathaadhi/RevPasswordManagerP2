package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.model.Category;
import com.rev.revpasswordmanagerp2.model.PasswordEntry;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.model.VerificationCode;
import com.rev.revpasswordmanagerp2.repository.PasswordEntryRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.util.EncryptionUtil;
import com.rev.revpasswordmanagerp2.util.PasswordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VaultServiceImpl implements VaultService {

    private final PasswordEntryRepository passwordEntryRepository;
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;
    private final VerificationService verificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ================= ADD PASSWORD =================

    @Override
    public String addPassword(VaultRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordEntry entry = new PasswordEntry();

        entry.setAccountName(request.getAccountName());
        entry.setWebsiteUrl(request.getWebsite());
        entry.setAccountUsername(request.getUsername());

        String safePassword = (request.getPassword() == null || request.getPassword().isEmpty())
                ? "TEMP123!"
                : request.getPassword();

        entry.setEncryptedPassword(encryptionUtil.encrypt(safePassword));

        try {
            entry.setCategory(Category.valueOf(request.getCategory().toUpperCase()));
        } catch (Exception e) {
            entry.setCategory(Category.OTHER);
        }

        entry.setNotes(request.getNotes());
        entry.setFavorite(false);
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUpdatedAt(LocalDateTime.now());
        entry.setUser(user);

        passwordEntryRepository.save(entry);

        return "Password Added Successfully";
    }

    // ================= GET ALL =================

    @Override
    public List<PasswordEntryDTO> getAll(String usernameOrEmail) {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEntryRepository
                .findByUserId(user.getId())
                .stream()
                .map(PasswordMapper::toDTO)
                .toList();
    }

    // ================= FAVORITES =================

    @Override
    public List<PasswordEntryDTO> getFavorites(String usernameOrEmail){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEntryRepository
                .findByUserId(user.getId())
                .stream()
                .filter(entry -> Boolean.TRUE.equals(entry.getFavorite()))
                .map(PasswordMapper::toDTO)
                .toList();
    }

    @Override
    public String favorite(Long id, boolean value){

        PasswordEntry entry = passwordEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        entry.setFavorite(value);
        passwordEntryRepository.save(entry);

        return "Favorite Updated";
    }

    // ================= DELETE =================
    @Override
    public String delete(DeletePasswordRequest request) {

        // ===== VALIDATION =====
        if (request.getEntryId() == null)
            throw new RuntimeException("Entry ID missing");

        if (request.getUsernameOrEmail() == null)
            throw new RuntimeException("User missing");

        if (request.getMasterPassword() == null)
            throw new RuntimeException("Master password missing");

        if (request.getVerificationCode() == null)
            throw new RuntimeException("Verification code missing");


        // ===== FIND PASSWORD ENTRY =====
        PasswordEntry entry = passwordEntryRepository
                .findById(request.getEntryId())
                .orElseThrow(() ->
                        new RuntimeException("Password entry not found"));


        // ===== FIND USER =====
        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        // ===== VERIFY MASTER PASSWORD =====
        if (!passwordEncoder.matches(
                request.getMasterPassword(),
                user.getMasterPasswordHash())) {

            throw new RuntimeException("Invalid master password");
        }


        // ===== VERIFY OTP USING SERVICE =====
        try {

            verificationService.validateCode(
                    request.getUsernameOrEmail(),
                    request.getVerificationCode()
            );

        } catch (Exception e) {

            throw new RuntimeException("OTP Invalid or Expired");

        }

        // ===== DELETE =====
        passwordEntryRepository.delete(entry);

        return "Password deleted successfully";
    }

    // ================= UPDATE =================

    @Override
    public String update(Long id, VaultRequest request) {

        PasswordEntry entry = passwordEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        entry.setAccountName(request.getAccountName());
        entry.setWebsiteUrl(request.getWebsite());
        entry.setAccountUsername(request.getUsername());

        String safePassword = (request.getPassword() == null || request.getPassword().isEmpty())
                ? encryptionUtil.decrypt(entry.getEncryptedPassword())
                : request.getPassword();

        entry.setEncryptedPassword(encryptionUtil.encrypt(safePassword));

        try {
            entry.setCategory(Category.valueOf(request.getCategory().toUpperCase()));
        } catch (Exception e) {
            entry.setCategory(Category.OTHER);
        }

        entry.setNotes(request.getNotes());
        entry.setUpdatedAt(LocalDateTime.now());

        passwordEntryRepository.save(entry);

        return "Password Updated Successfully";
    }

    // ================= FILTER =================

    @Override
    public List<PasswordEntryDTO> filter(String usernameOrEmail, String category){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category categoryEnum = Category.valueOf(category.toUpperCase());

        return passwordEntryRepository
                .findByUserIdAndCategory(user.getId(), categoryEnum)
                .stream()
                .map(PasswordMapper::toDTO)
                .toList();
    }

    // ================= SEARCH =================

    @Override
    public List<PasswordEntryDTO> search(String usernameOrEmail,String keyword){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEntryRepository
                .findByUserIdAndAccountNameContainingIgnoreCase(
                        user.getId(), keyword)
                .stream()
                .map(PasswordMapper::toDTO)
                .toList();
    }

    // ================= SORT =================

    @Override
    public List<PasswordEntryDTO> sort(String usernameOrEmail, String sortBy){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PasswordEntry> entries =
                passwordEntryRepository.findByUserId(user.getId());

        switch (sortBy.toLowerCase()){
            case "name":
                entries.sort(Comparator.comparing(PasswordEntry::getAccountName));
                break;
            case "created":
                entries.sort(Comparator.comparing(PasswordEntry::getCreatedAt));
                break;
            case "updated":
                entries.sort(Comparator.comparing(PasswordEntry::getUpdatedAt));
                break;
            default:
                throw new RuntimeException("Invalid sort option");
        }

        return entries.stream()
                .map(PasswordMapper::toDTO)
                .toList();
    }

    // ================= VIEW WITH MASTER PASSWORD =================

    @Override
    public ViewPasswordResponseDTO viewWithVerification(ViewPasswordRequest request){

        PasswordEntry entry = passwordEntryRepository
                .findById(request.getEntryId())
                .orElseThrow(() -> new RuntimeException("Password not found"));

        User user = entry.getUser();

        // Validate master password
        if(!passwordEncoder.matches(
                request.getMasterPassword(),
                user.getMasterPasswordHash())){

            throw new RuntimeException("Invalid master password");
        }

        // Validate verification code
        verificationService.validateCode(
                request.getUsernameOrEmail(),
                request.getVerificationCode()
        );

        // Decrypt password
        String decrypted =
                encryptionUtil.decrypt(entry.getEncryptedPassword());

        return ViewPasswordResponseDTO.builder()
                .id(entry.getId())
                .decryptedPassword(decrypted)
                .build();
    }

    // ================= OLD PASSWORDS =================

    @Override
    public List<PasswordEntryDTO> getOldPasswords(String usernameOrEmail){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime ninetyDaysAgo = LocalDateTime.now().minusDays(90);

        return passwordEntryRepository
                .findByUserId(user.getId())
                .stream()
                .filter(entry ->
                        entry.getUpdatedAt() != null &&
                                entry.getUpdatedAt().isBefore(ninetyDaysAgo))
                .map(PasswordMapper::toDTO)
                .toList();
    }

    // ================= EXPORT  =================
    @Override
    public List<VaultExportDTO> exportVault(
            ExportVaultRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // master password validation
        if (!passwordEncoder.matches(
                request.getMasterPassword(),
                user.getMasterPasswordHash())) {

            throw new RuntimeException("Invalid master password");
        }

        verificationService.validateCode(
                request.getUsernameOrEmail(),
                request.getVerificationCode()
        );

        List<PasswordEntry> entries =
                passwordEntryRepository.findByUserId(user.getId());

        return entries.stream()
                .map(entry -> new VaultExportDTO(
                        entry.getAccountName(),
                        entry.getWebsiteUrl(),
                        entry.getAccountUsername(),
                        encryptionUtil.decrypt(
                                entry.getEncryptedPassword()
                        ),
                        entry.getCategory().name(),
                        entry.getNotes()
                ))
                .toList();
    }

    // ================= IMPORT =================

    @Override
    public void importVault(
            ImportVaultRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        verificationService.validateCode(
                request.getUsernameOrEmail(),
                request.getVerificationCode()
        );

        for (VaultExportDTO dto : request.getVaultData()) {

            PasswordEntry entry = new PasswordEntry();

            entry.setUser(user);
            entry.setAccountName(dto.getAccountName());
            entry.setWebsiteUrl(dto.getWebsite());
            entry.setAccountUsername(dto.getUsername());

            // CORRECT ENCRYPTION
            entry.setEncryptedPassword(
                    encryptionUtil.encrypt(dto.getPassword())
            );

            entry.setCategory(
                    Category.valueOf(dto.getCategory())
            );

            entry.setNotes(dto.getNotes());
            entry.setFavorite(false);
            entry.setCreatedAt(LocalDateTime.now());
            entry.setUpdatedAt(LocalDateTime.now());

            passwordEntryRepository.save(entry);
        }
    }
}