package com.rev.revpasswordmanagerp2.repository;

import com.rev.revpasswordmanagerp2.model.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    List<PasswordHistory> findByUserId(Long userId);

    List<PasswordHistory> findByUserIdAndOldPassword(Long userId, String oldPassword);

}
