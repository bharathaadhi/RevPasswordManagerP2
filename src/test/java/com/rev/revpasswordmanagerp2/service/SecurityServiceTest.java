package com.rev.revpasswordmanagerp2.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SecurityServiceTest {

    private final SecurityService securityService =
            new SecurityServiceImpl(null, null, null);

    @Test
    void testGeneratePassword() {
        String password = securityService.generatePassword(
                12, true, true, true, true, false);

        assertNotNull(password);
        assertEquals(12, password.length());
    }
}