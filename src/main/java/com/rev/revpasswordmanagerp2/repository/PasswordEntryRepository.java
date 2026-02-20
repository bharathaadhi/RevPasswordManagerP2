package com.rev.revpasswordmanagerp2.repository;

import com.rev.revpasswordmanagerp2.entity.PasswordEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Long> {
    List<PasswordEntry> findByUserIdAndAccountNameContainingIgnoreCase(Long userId,String keyword);

    List<PasswordEntry> findByUserIdAndWebsiteContainingIgnoreCase(Long userId,String keyword);

    List<PasswordEntry> findByUserIdAndUsernameContainingIgnoreCase(Long userId,String keyword);

    List<PasswordEntry> findByUserIdAndCategory(Long userId,String category);

    List<PasswordEntry> findByUserIdAndFavoriteTrue(Long userId);

    List<PasswordEntry> findByUserId(Long id);
}
