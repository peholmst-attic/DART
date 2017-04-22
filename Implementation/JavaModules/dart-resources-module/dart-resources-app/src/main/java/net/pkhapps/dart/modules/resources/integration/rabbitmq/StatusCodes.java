package net.pkhapps.dart.modules.resources.integration.rabbitmq;

/**
 * TODO Document me
 */
public final class StatusCodes {

    public static final int SYSTEM_ERROR = 500;
    public static final int OK = 200;
    public static final int FORBIDDEN = 403;

    public static final String STATUS_CODE_HEADER = "statusCode";
    public static final String STATUS_MESSAGE_HEADER = "statusMessage";


    private StatusCodes() {
    }
}
