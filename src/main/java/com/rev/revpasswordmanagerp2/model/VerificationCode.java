package com.rev.revpasswordmanagerp2.model;

import jakarta.persistence.*;
import lombok.*;
import com.rev.revpasswordmanagerp2.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String code;

    private LocalDateTime expiryTime;

    private Boolean used = false;
}

