package com.rev.revpasswordmanagerp2.repository;

import com.rev.revpasswordmanagerp2.model.Category;
import com.rev.revpasswordmanagerp2.model.PasswordEntry;
import com.rev.revpasswordmanagerp2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Long> {

    List<PasswordEntry> findByUser(User user);

    long countByUser(User user);

    List<PasswordEntry> findByUserIdAndAccountNameContainingIgnoreCase(Long userId, String keyword);

    List<PasswordEntry> findByUserIdAndWebsiteUrlContainingIgnoreCase(Long userId, String keyword);

    List<PasswordEntry> findByUserIdAndAccountUsernameContainingIgnoreCase(Long userId, String keyword);

    List<PasswordEntry> findByUserIdAndCategory(Long userId, Category category);

    List<PasswordEntry> findByUserIdAndFavoriteTrue(Long userId);

    List<PasswordEntry> findByUserId(Long userId);
}