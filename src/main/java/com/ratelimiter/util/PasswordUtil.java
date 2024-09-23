package com.ratelimiter.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordUtil {

    private static final int SALT_LENGTH = 16;  // 16 bytes for salt
    private static final int HASH_LENGTH = 256; // 256 bits
    private static final int ITERATIONS = 10000;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private PasswordUtil() {}

    /**
     * Generate hashed password with salt
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);

        byte[] hash = hashPasswordWithSalt(password, salt);

        byte[] saltAndHash = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, saltAndHash, 0, salt.length);
        System.arraycopy(hash, 0, saltAndHash, salt.length, hash.length);

        return Base64.getEncoder().encodeToString(saltAndHash);
    }

    public static boolean validatePassword(String password, String storedPasswordHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] saltAndHash = Base64.getDecoder().decode(storedPasswordHash);

        byte[] salt = new byte[SALT_LENGTH];
        System.arraycopy(saltAndHash, 0, salt, 0, SALT_LENGTH);

        byte[] storedHash = new byte[saltAndHash.length - SALT_LENGTH];
        System.arraycopy(saltAndHash, SALT_LENGTH, storedHash, 0, storedHash.length);

        byte[] hash = hashPasswordWithSalt(password, salt);

        return slowEquals(hash, storedHash);
    }

    private static byte[] hashPasswordWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        return factory.generateSecret(spec).getEncoded();
    }

    // Time-constant comparison to avoid timing attacks
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
