package com.rev.revpasswordmanagerp2.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class PasswordEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountName;
    private String website;
    private String username;
    private String password;
    private String category;
    private boolean favorite;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String getWebsiteUrl() {
        return "";
    }

    public Object getNotes() {
        return null;
    }
}
