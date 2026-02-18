package com.rev.revpasswordmanagerp2.repository;

import com.rev.revpasswordmanagerp2.model.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Long> {
}
