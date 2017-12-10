package net.pkhapps.dart.modules.base.rabbitmq.messaging.server;

import net.pkhapps.dart.modules.base.rabbitmq.messaging.StatusCodes;

/**
 * Exception thrown by a broker when a handler throws an exception that should be reported back to the client that
 * sent the message.
 */
public class HandlerException extends Exception {

    private final int errorCode;

    public HandlerException(String message) {
        super(message);
        this.errorCode = StatusCodes.SYSTEM_ERROR;
    }

    public HandlerException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = StatusCodes.SYSTEM_ERROR;
    }

    public HandlerException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public HandlerException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code reported by the handler.
     */
    public int getErrorCode() {
        return errorCode;
    }
}
