package com.rev.revpasswordmanagerp2.repository;

import com.rev.revpasswordmanagerp2.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    // simple secure method
    Optional<VerificationCode> findByUserIdAndCode(Long userId, String code);

}