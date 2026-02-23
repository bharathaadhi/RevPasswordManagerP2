package com.rev.revpasswordmanagerp2.model;

import jakarta.persistence.*;
import lombok.*;

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

    private String email;

    @Column(nullable = false)
    private String password;

    // MASTER PASSWORD (for vault access validation)
    private String masterPassword;

    // TWO FACTOR AUTH ENABLED
    private Boolean twoFactorEnabled = false;

    private Boolean isActive = true;
}