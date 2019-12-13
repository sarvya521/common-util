package com.sp.common.util;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class Passwords {
    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    /**
     * static utility class
     */
    private Passwords() {
    }

    /**
     * Returns a random salt to be used to hash a password.
     *
     * @return a 16 bytes random salt
     */
    public static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String salt, String password) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
        Arrays.fill(password.toCharArray(), Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey secretKey = skf.generateSecret(spec);
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public static final String SALT = "SARVESH";

    public static int hashPassword(String password) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), ITERATIONS, KEY_LENGTH);
        Arrays.fill(password.toCharArray(), Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey secretKey = skf.generateSecret(spec);
            String hash = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            return convertStringToInt(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Returns true if the given password and salt match the hashed value, false otherwise.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password     the password to check
     * @param salt         the salt used to hash the password
     * @param expectedHash the expected hashed value of the password
     * @return true if the given password and salt match the hashed value, false otherwise
     */
    public static boolean isExpectedPassword(String password, String salt, String expectedHash) {
        String pwdHash = hashPassword(password, salt);
        return pwdHash.equals(expectedHash);
    }

    /**
     * Generates a random password of a given length, using digits.
     *
     * @param length the length of the password
     * @return a random password
     */
    public static String generateRandomPassword(int length) {
        return String.valueOf(length < 1 ? 0 : new Random()
            .nextInt((9 * (int) Math.pow(10, length - 1)) - 1)
            + (int) Math.pow(10, length - 1));
    }

    public static int convertStringToInt(String str) {
        char[] arr = str.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char ch : arr) {
            builder.append((int) ch);
        }
        return Integer.parseInt(builder.toString());
    }
}