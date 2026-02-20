package com.rev.revpasswordmanagerp2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "password_history")
public class PasswordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oldPassword;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
