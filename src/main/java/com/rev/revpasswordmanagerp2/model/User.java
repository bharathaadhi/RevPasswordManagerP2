package com.rev.revpasswordmanagerp2.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    // ✅ ONLY ONE COLUMN ANNOTATION
    @Column(name = "master_password_hash", nullable = false)
    private String masterPasswordHash;

    private Boolean twoFactorEnabled = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    // ✅ RELATION WITH SECURITY QUESTIONS
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SecurityQuestion> securityQuestions;
}
