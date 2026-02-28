package com.rev.revpasswordmanagerp2.repository;

import com.rev.revpasswordmanagerp2.model.UserSecurityAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSecurityAnswerRepository
        extends JpaRepository<UserSecurityAnswer, Long> {

    List<UserSecurityAnswer> findByUserId(Long userId);
}