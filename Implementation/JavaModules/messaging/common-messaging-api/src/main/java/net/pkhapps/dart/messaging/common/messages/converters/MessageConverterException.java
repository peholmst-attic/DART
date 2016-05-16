package net.pkhapps.dart.messaging.common.messages.converters;

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
