package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.RegisterRequest;
import com.rev.revpasswordmanagerp2.dto.SecurityQuestionDTO;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.SecurityQuestionRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityQuestionRepository securityQuestionRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldReturnSuccessMessage() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@gmail.com");
        request.setMasterPassword("Strong@123");

        List<SecurityQuestionDTO> questions = new ArrayList<>();

        SecurityQuestionDTO q1 = new SecurityQuestionDTO();
        q1.setQuestion("Q1");
        q1.setAnswer("A1");

        SecurityQuestionDTO q2 = new SecurityQuestionDTO();
        q2.setQuestion("Q2");
        q2.setAnswer("A2");

        SecurityQuestionDTO q3 = new SecurityQuestionDTO();
        q3.setQuestion("Q3");
        q3.setAnswer("A3");

        questions.add(q1);
        questions.add(q2);
        questions.add(q3);

        request.setSecurityQuestions(questions);

        when(passwordEncoder.encode(anyString()))
                .thenReturn("encodedPassword");

        when(userRepository.save(any()))
                .thenReturn(new User());

        when(securityQuestionRepository.save(any()))
                .thenReturn(null);

        String result = authService.registerUser(request);

        assertNotNull(result);
    }
}