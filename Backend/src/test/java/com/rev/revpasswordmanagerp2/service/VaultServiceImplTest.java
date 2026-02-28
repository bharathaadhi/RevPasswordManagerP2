package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.VaultRequest;
import com.rev.revpasswordmanagerp2.model.User;
import com.rev.revpasswordmanagerp2.repository.PasswordEntryRepository;
import com.rev.revpasswordmanagerp2.repository.UserRepository;
import com.rev.revpasswordmanagerp2.util.EncryptionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VaultServiceImplTest {

    @Mock
    private PasswordEntryRepository passwordEntryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionUtil encryptionUtil;

    @InjectMocks
    private VaultServiceImpl vaultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPassword_ShouldReturnMessage() {

        VaultRequest request = new VaultRequest();
        request.setUsernameOrEmail("testuser");
        request.setAccountName("Google");
        request.setPassword("Test@123");
        request.setCategory("SOCIAL_MEDIA");

        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsernameOrEmail(any(), any()))
                .thenReturn(Optional.of(user));

        when(encryptionUtil.encrypt(anyString()))
                .thenReturn("encryptedPassword");

        String result = vaultService.addPassword(request);

        assertNotNull(result);
    }
}