package net.pkhapps.dart.modules.resources.integration.rabbitmq;

/**
 * Exception thrown by a broker when the user did not have permission to perform the requested operation.
 */
public class AccessDeniedException extends Exception {

    public AccessDeniedException() {
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
