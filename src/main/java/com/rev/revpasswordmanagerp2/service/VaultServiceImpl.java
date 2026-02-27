package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.model.Category;
import com.rev.revpasswordmanagerp2.model.PasswordEntry;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.PasswordEntryRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.util.EncryptionUtil;
import com.rev.revpasswordmanagerp2.util.PasswordMapper;

import lombok.RequiredArgsConstructor;

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
    private final PasswordEncoder passwordEncoder;

    // ================= ADD =================
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

        String password =
                (request.getPassword()==null || request.getPassword().isEmpty())
                        ? "TEMP123!"
                        : request.getPassword();

        entry.setEncryptedPassword(encryptionUtil.encrypt(password));
        entry.setCategory(Category.valueOf(request.getCategory().toUpperCase()));
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

        return passwordEntryRepository.findByUserId(user.getId())
                .stream()
                .map(PasswordMapper::toDTO)
                .toList();
    }

    // ================= OLD PASSWORD =================
    @Override
    public List<PasswordEntryDTO> getOldPasswords(String usernameOrEmail){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime ninetyDaysAgo =
                LocalDateTime.now().minusDays(90);

        return passwordEntryRepository.findByUserId(user.getId())
                .stream()
                .filter(e -> e.getUpdatedAt()!=null &&
                        e.getUpdatedAt().isBefore(ninetyDaysAgo))
                .map(PasswordMapper::toDTO)
                .toList();
    }

    // ================= FAVORITE =================
    @Override
    public String favorite(Long id, boolean value){
        PasswordEntry entry = passwordEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        entry.setFavorite(value);
        passwordEntryRepository.save(entry);

        return "Favorite Updated";
    }

    @Override
    public List<PasswordEntryDTO> getFavorites(String usernameOrEmail){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail,usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEntryRepository.findByUserId(user.getId())
                .stream()
                .filter(e->Boolean.TRUE.equals(e.getFavorite()))
                .map(PasswordMapper::toDTO)
                .toList();
    }

    // ================= UPDATE =================
    @Override
    public String update(Long id, VaultRequest request){

        PasswordEntry entry = passwordEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        entry.setAccountName(request.getAccountName());
        entry.setWebsiteUrl(request.getWebsite());
        entry.setAccountUsername(request.getUsername());

        String password =
                request.getPassword()==null || request.getPassword().isEmpty()
                        ? encryptionUtil.decrypt(entry.getEncryptedPassword())
                        : request.getPassword();

        entry.setEncryptedPassword(encryptionUtil.encrypt(password));
        entry.setUpdatedAt(LocalDateTime.now());

        passwordEntryRepository.save(entry);

        return "Password Updated Successfully";
    }

    // ================= DELETE =================
    @Override
    public String delete(DeletePasswordRequest request){

        PasswordEntry entry =
                passwordEntryRepository.findById(request.getEntryId())
                        .orElseThrow(() -> new RuntimeException("Entry not found"));

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(
                request.getMasterPassword(),
                user.getMasterPasswordHash()))
            throw new RuntimeException("Invalid master password");

        verificationService.validateCode(
                request.getUsernameOrEmail(),
                request.getVerificationCode());

        passwordEntryRepository.delete(entry);

        return "Password deleted successfully";
    }

    // ================= FILTER =================
    @Override
    public List<PasswordEntryDTO> filter(String usernameOrEmail,String category){

        User user=userRepository
                .findByUsernameOrEmail(usernameOrEmail,usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category cat=Category.valueOf(category.toUpperCase());

        return passwordEntryRepository
                .findByUserIdAndCategory(user.getId(),cat)
                .stream()
                .map(PasswordMapper::toDTO)
                .toList();
    }

    // ================= SEARCH =================
    @Override
    public List<PasswordEntryDTO> search(String usernameOrEmail,String keyword){

        User user=userRepository
                .findByUsernameOrEmail(usernameOrEmail,usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEntryRepository
                .findByUserIdAndAccountNameContainingIgnoreCase(
                        user.getId(),keyword)
                .stream()
                .map(PasswordMapper::toDTO)
                .toList();
    }

    // ================= SORT =================
    @Override
    public List<PasswordEntryDTO> sort(String usernameOrEmail,String sortBy){

        User user=userRepository
                .findByUsernameOrEmail(usernameOrEmail,usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PasswordEntry> list=
                passwordEntryRepository.findByUserId(user.getId());

        switch(sortBy.toLowerCase()){
            case "name" -> list.sort(Comparator.comparing(PasswordEntry::getAccountName));
            case "created" -> list.sort(Comparator.comparing(PasswordEntry::getCreatedAt));
            case "updated" -> list.sort(Comparator.comparing(PasswordEntry::getUpdatedAt));
        }

        return list.stream().map(PasswordMapper::toDTO).toList();
    }

    // ================= VIEW =================
    @Override
    public ViewPasswordResponseDTO viewWithVerification(ViewPasswordRequest request){

        PasswordEntry entry=passwordEntryRepository.findById(request.getEntryId())
                .orElseThrow(() -> new RuntimeException("Password not found"));

        verificationService.validateCode(
                request.getUsernameOrEmail(),
                request.getVerificationCode());

        String decrypted=
                encryptionUtil.decrypt(entry.getEncryptedPassword());

        return ViewPasswordResponseDTO.builder()
                .id(entry.getId())
                .decryptedPassword(decrypted)
                .build();
    }

    // ================= EXPORT =================
    @Override
    public List<VaultExportDTO> exportVault(ExportVaultRequest request){

        User user=userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        verificationService.validateCode(
                request.getUsernameOrEmail(),
                request.getVerificationCode());

        return passwordEntryRepository.findByUserId(user.getId())
                .stream()
                .map(e->new VaultExportDTO(
                        e.getAccountName(),
                        e.getWebsiteUrl(),
                        e.getAccountUsername(),
                        encryptionUtil.decrypt(e.getEncryptedPassword()),
                        e.getCategory().name(),
                        e.getNotes()))
                .toList();
    }

    // ================= IMPORT =================
    @Override
    public void importVault(ImportVaultRequest request){

        User user=userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        verificationService.validateCode(
                request.getUsernameOrEmail(),
                request.getVerificationCode());

        for(VaultExportDTO dto:request.getVaultData()){

            PasswordEntry entry=new PasswordEntry();

            entry.setUser(user);
            entry.setAccountName(dto.getAccountName());
            entry.setWebsiteUrl(dto.getWebsite());
            entry.setAccountUsername(dto.getUsername());
            entry.setEncryptedPassword(
                    encryptionUtil.encrypt(dto.getPassword()));
            entry.setCategory(Category.valueOf(dto.getCategory()));
            entry.setNotes(dto.getNotes());
            entry.setCreatedAt(LocalDateTime.now());
            entry.setUpdatedAt(LocalDateTime.now());

            passwordEntryRepository.save(entry);
        }
    }
}