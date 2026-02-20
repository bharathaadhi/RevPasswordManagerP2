package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.VaultRequest;
import com.rev.revpasswordmanagerp2.entity.PasswordEntry;
import com.rev.revpasswordmanagerp2.entity.User;
import com.rev.revpasswordmanagerp2.repository.PasswordEntryRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VaultServiceImpl implements VaultService {

    private final PasswordEntryRepository passwordEntryRepository;
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;   // ✅ added

    @Override
    public String addPassword(VaultRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordEntry entry = new PasswordEntry();
        entry.setAccountName(request.getAccountName());
        entry.setWebsite(request.getWebsite());
        entry.setUsername(request.getUsername());

        entry.setPassword(encryptionUtil.encrypt(request.getPassword()));

        entry.setCategory(request.getCategory());
        entry.setFavorite(false);
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUser(user);

        passwordEntryRepository.save(entry);

        return "Password Added Successfully";
    }
    @Override
    public List<PasswordEntry> favorites(String usernameOrEmail){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEntryRepository
                .findByUserIdAndFavoriteTrue(user.getId());
    }


    @Override
    public List<PasswordEntry> getAll(String usernameOrEmail) {

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEntryRepository.findByUserId(user.getId());
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
        entry.setWebsite(request.getWebsite());
        entry.setUsername(request.getUsername());


        entry.setPassword(encryptionUtil.encrypt(request.getPassword()));

        entry.setCategory(request.getCategory());
        entry.setUpdatedAt(LocalDateTime.now());

        passwordEntryRepository.save(entry);

        return "Password Updated Successfully";
    }
    @Override
    public List<PasswordEntry> filter(String usernameOrEmail, String category){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEntryRepository
                .findByUserIdAndCategory(user.getId(),category);
    }


    @Override
    public List<PasswordEntry> search(String usernameOrEmail,String keyword){

        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEntryRepository
                .findByUserIdAndAccountNameContainingIgnoreCase(user.getId(),keyword);
    }


}