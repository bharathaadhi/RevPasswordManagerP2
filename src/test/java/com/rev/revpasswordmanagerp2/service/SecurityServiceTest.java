package com.rev.revpasswordmanagerp2.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityServiceTest {

    private SecurityService securityService = new SecurityService();

    @Test
    void checkStrength_ShouldReturnStrong() {

        String strength = securityService.checkStrength("Strong@123");

        assertEquals("Strong", strength);
    }
}