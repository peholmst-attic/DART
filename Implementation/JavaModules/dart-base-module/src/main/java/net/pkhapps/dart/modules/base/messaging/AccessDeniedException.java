package net.pkhapps.dart.modules.base.messaging;

/**
 * Exception thrown/returned when a user does not have permission to perform an operation.
 */
public class AccessDeniedException extends Exception {

    public AccessDeniedException() {
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
