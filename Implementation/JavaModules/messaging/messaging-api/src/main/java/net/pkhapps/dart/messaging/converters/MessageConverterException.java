package net.pkhapps.dart.messaging.converters;

import java.io.IOException;

public class MessageConverterException extends IOException {
    public MessageConverterException() {
    }

    public MessageConverterException(String message) {
        super(message);
    }

    public MessageConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
