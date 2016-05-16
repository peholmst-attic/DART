package net.pkhapps.dart.messaging.facade;

public class DestinationMissingException extends SendFailureException {

    public DestinationMissingException() {
    }

    public DestinationMissingException(String message) {
        super(message);
    }

    public DestinationMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
