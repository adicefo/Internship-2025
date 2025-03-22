package com.example.internship_api.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {

    private static final int SALT_LENGTH = 16;

    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String generateHash(String salt, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            byte[] saltBytes = Base64.getDecoder().decode(salt);
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

            byte[] combined = new byte[saltBytes.length + passwordBytes.length];

            System.arraycopy(saltBytes, 0, combined, 0, saltBytes.length);
            System.arraycopy(passwordBytes, 0, combined, saltBytes.length, passwordBytes.length);

            byte[] hashedBytes = digest.digest(combined);
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static boolean verifyPassword(String enteredPassword, String storedHash, String storedSalt) {
        String enteredHash = generateHash(storedSalt, enteredPassword);
        return enteredHash.equals(storedHash);
    }
}

