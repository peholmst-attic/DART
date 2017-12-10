package net.pkhapps.dart.modules.base.rabbitmq.messaging;

import java.io.IOException;

/**
 * TODO Document me
 */
public class MessageConversionException extends IOException {

    public MessageConversionException() {
    }

    public MessageConversionException(String message) {
        super(message);
    }

    public MessageConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageConversionException(Throwable cause) {
        super(cause);
    }
}
