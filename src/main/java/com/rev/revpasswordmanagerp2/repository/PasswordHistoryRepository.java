package com.rev.revpasswordmanagerp2.repository;

import com.rev.revpasswordmanagerp2.model.PasswordHistory;
import com.rev.revpasswordmanagerp2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    List<PasswordHistory> findByUser(User user);

    List<PasswordHistory> findByUserAndPasswordHash(User user, String passwordHash);

}
