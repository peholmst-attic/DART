package net.pkhapps.dart.modules.resources.integration.rabbitmq;

/**
 * Exception thrown by a broker when it cannot find a suitable handler for a message.
 */
public class HandlerNotFoundException extends Exception {

    public HandlerNotFoundException() {
    }

    public HandlerNotFoundException(String message) {
        super(message);
    }
}
