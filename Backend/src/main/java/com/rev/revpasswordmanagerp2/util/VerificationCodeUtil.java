package com.rev.revpasswordmanagerp2.util;

import java.time.LocalDateTime;
import java.util.Random;

public class VerificationCodeUtil {

    public static String generateCode() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    public static LocalDateTime generateExpiryTime() {
        return LocalDateTime.now().plusMinutes(5);
    }
    public static boolean isExpired(LocalDateTime expiryTime) {
        return LocalDateTime.now().isAfter(expiryTime);
    }
}
