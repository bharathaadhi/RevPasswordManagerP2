package com.rev.revpasswordmanagerp2.model;

import com.rev.revpasswordmanagerp2.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "security_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answerHash;

    public void setAnswer(String answer, PasswordEncoder encoder) {
        this.answerHash = encoder.encode(answer);
    }
}