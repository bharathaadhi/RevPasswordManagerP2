package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.PasswordEntryDTO;
import com.rev.revpasswordmanagerp2.dto.VaultRequest;
import com.rev.revpasswordmanagerp2.model.Category;
import com.rev.revpasswordmanagerp2.model.PasswordEntry;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.PasswordEntryRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.util.EncryptionUtil;
import com.rev.revpasswordmanagerp2.util.PasswordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VaultServiceImpl implements VaultService {

    private final PasswordEntryRepository passwordEntryRepository;
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;

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
        entry.setEncryptedPassword(encryptionUtil.encrypt(request.getPassword()));
        entry.setCategory(Category.valueOf(request.getCategory().toUpperCase()));
        entry.setFavorite(false);
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUser(user);

        passwordEntryRepository.save(entry);

        return "Password Added Successfully";
    }

    @Override
    public List<PasswordEntryDTO> favorites(String usernameOrEmail){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEntryRepository
                .findByUserIdAndFavoriteTrue(user.getId())
                .stream()
                .map(PasswordMapper::toDTO)
                .toList();
    }

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

    @Override
    public String favorite(Long id, boolean value){

        PasswordEntry entry = passwordEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        entry.setFavorite(value);
        passwordEntryRepository.save(entry);

        return "Favorite Updated";
    }

    @Override
    public String delete(Long id) {

        passwordEntryRepository.deleteById(id);
        return "Password Deleted";
    }

    @Override
    public String update(Long id, VaultRequest request) {

        PasswordEntry entry = passwordEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        entry.setAccountName(request.getAccountName());
        entry.setWebsiteUrl(request.getWebsite());
        entry.setAccountUsername(request.getUsername());
        entry.setEncryptedPassword(encryptionUtil.encrypt(request.getPassword()));
        entry.setCategory(Category.valueOf(request.getCategory().toUpperCase()));
        entry.setUpdatedAt(LocalDateTime.now());

        passwordEntryRepository.save(entry);

        return "Password Updated Successfully";
    }

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

    @Override
    public List<PasswordEntryDTO> search(String usernameOrEmail,String keyword){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEntryRepository
                .findByUserIdAndAccountNameContainingIgnoreCase(user.getId(), keyword)
                .stream()
                .map(PasswordMapper::toDTO)
                .toList();
    }
}