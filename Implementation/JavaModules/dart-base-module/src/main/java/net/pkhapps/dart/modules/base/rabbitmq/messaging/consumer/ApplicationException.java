package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer;

/**
 * TODO Document me!
 */
public class ApplicationException extends Exception {

    private final int errorCode;

    public ApplicationException(int errorCode) {
        this.errorCode = errorCode;
    }

    public ApplicationException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
