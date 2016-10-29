package net.pkhapps.dart.poc;

public final class Constants {

    private Constants() {
    }

    public static final String HOST = "localhost";
    public static final String QUEUE_NAME = "hello";

    public static final long CONFIRMATION_TIMEOUT_MS = 1000; // 1 second
    public static final String MESSAGE_EXPIRATION = "10000"; // 10 seconds
}
