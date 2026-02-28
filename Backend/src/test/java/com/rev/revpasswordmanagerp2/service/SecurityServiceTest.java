package com.rev.revpasswordmanagerp2.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityServiceTest {

    SecurityServiceImpl service =
            new SecurityServiceImpl(null, null, null, null, null, null);

    @Test
    void testGeneratePassword() {

        String password = service.generatePassword(
                12, true, true, true, true, false
        );

        assertNotNull(password);
        assertEquals(12, password.length());
    }
}