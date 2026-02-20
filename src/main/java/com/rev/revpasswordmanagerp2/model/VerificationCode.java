package com.rev.revpasswordmanagerp2.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String code;

    @Column(name = "expiry_time")
    private LocalDateTime expiryTime;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    private Boolean used = false;
}
