package net.pkhapps.dart.server.certificatemanager.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;

public class CSRToken implements Serializable {

    private final Instant generationTime;
    private final Instant expirationTime;
    private final long runningNumber;
    private final String hash;

    public CSRToken(Instant generationTime, long runningNumber) {
        this.generationTime = generationTime;
        this.runningNumber = runningNumber;
        this.expirationTime = generationTime.plusSeconds(60);
        this.hash = createHash(String.format("%s:%s:%d", generationTime, expirationTime, runningNumber));
    }

    private static String createHash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return String.format("%064x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Cannot create SHA-256 hashes");
        }
    }

    public Instant getGenerationTime() {
        return generationTime;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public long getRunningNumber() {
        return runningNumber;
    }

    public String getHash() {
        return hash;
    }

    public boolean isExpired(Clock clock) {
        return expirationTime.isBefore(clock.instant());
    }
}
