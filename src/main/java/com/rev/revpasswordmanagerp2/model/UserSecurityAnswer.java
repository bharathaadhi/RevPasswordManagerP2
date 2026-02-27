package com.rev.revpasswordmanagerp2.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_security_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSecurityAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="question_id")
    private SecurityQuestion question;

    @Column(nullable = false)
    private String answerHash;
}