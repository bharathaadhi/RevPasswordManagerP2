package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.PasswordEntryRepository;
import com.rev.revpasswordmanagerp2.repository.PasswordHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditServiceTest {

    @Mock
    private PasswordEntryRepository passwordEntryRepository;

    @Mock
    private PasswordHistoryRepository passwordHistoryRepository;

    @InjectMocks
    private AuditServiceImpl auditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWeakPasswords_ShouldNotReturnNull() {

        User user = new User();
        user.setUsername("test");

        assertNotNull(auditService.getWeakPasswords(user));
    }
}