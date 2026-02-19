package com.rev.revpasswordmanagerp2.repository;

import com.rev.revpasswordmanagerp2.model.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Long> {
    List<SecurityQuestion> findByUserId(Long id);
}
