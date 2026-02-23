package com.rev.revpasswordmanagerp2.repository;

import com.rev.revpasswordmanagerp2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}