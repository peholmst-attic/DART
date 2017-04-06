package net.pkhapps.dart.modules.accounts.domain;

import org.bouncycastle.crypto.generators.BCrypt;
import org.bouncycastle.util.encoders.Hex;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

/**
 * Utilities for working with passwords.
 */
public final class PasswordUtil {

    // In a real world application, this should probably be calculated based on the production server's capabilities.
    private static final int DEFAULT_COST = 6;

    private static final SecureRandom random;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Could not create SecureRandom", ex);
        }
    }

    /**
     * Hashes the specified clear text password and returns a HEX string that also includes the salt.
     *
     * @param clearText the clear text password to hash.
     * @return a password string.
     */
    @NotNull
    public static String hashPassword(@NotNull String clearText) {
        Objects.requireNonNull(clearText, "clearText must not be null");

        byte[] salt = new byte[16];
        random.nextBytes(salt);

        byte[] encrypted = BCrypt.generate(clearText.getBytes(), salt, DEFAULT_COST);

        ByteBuffer byteBuffer = ByteBuffer.allocate(salt.length + encrypted.length);
        byteBuffer.put(salt);
        byteBuffer.put(encrypted);
        return Hex.toHexString(byteBuffer.array());
    }

    /**
     * Checks if the given password string and clear text password match.
     *
     * @param passwordString a password string returned by {@link #hashPassword(String)}.
     * @param clearText      the clear text password to check.
     * @return true if the passwords match, false if they don't.
     */
    public static final boolean verifyPassword(@NotNull String passwordString, @NotNull String clearText) {
        Objects.requireNonNull(passwordString, "passwordString must not be null");
        Objects.requireNonNull(clearText, "clearText must not be null");

        byte[] bytes = Hex.decode(passwordString);
        byte[] salt = Arrays.copyOf(bytes, 16);
        byte[] password = Arrays.copyOfRange(bytes, 16, bytes.length);
        byte[] encrypted = BCrypt.generate(clearText.getBytes(), salt, DEFAULT_COST);

        return Arrays.equals(password, encrypted);
    }
}
