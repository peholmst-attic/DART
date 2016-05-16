package net.pkhapps.dart.messaging.facade;

import java.io.IOException;

public class SendFailureException extends IOException {

    public SendFailureException() {
    }

    public SendFailureException(String message) {
        super(message);
    }

    public SendFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
