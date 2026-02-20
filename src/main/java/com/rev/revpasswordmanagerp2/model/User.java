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

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String phone;

    @Column(name = "master_password_hash", nullable = false)
    private String masterPasswordHash;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Boolean twoFactorEnabled = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SecurityQuestion> securityQuestions;
}