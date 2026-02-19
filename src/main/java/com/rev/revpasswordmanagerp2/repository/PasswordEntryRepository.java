package com.rev.revpasswordmanagerp2.repository;

import com.rev.revpasswordmanagerp2.model.PasswordEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Long> {

    long countByUserId(Long id);
}
