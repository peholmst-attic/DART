package net.pkhapps.dart.modules.resources.integration.rabbitmq;

/**
 * Created by peholmst on 22/04/2017.
 */
public final class Queues {

    public static final String EXCHANGE = "dart-resources-module";

    public static final String COMMANDS_ROUTING_KEY = "commands";
    public static final String COMMANDS_QUEUE = EXCHANGE + "." + COMMANDS_ROUTING_KEY;

    public static final String REQUESTS_ROUTING_KEY = "requests";
    public static final String REQUESTS_QUEUE = EXCHANGE + "." + REQUESTS_ROUTING_KEY;

    private Queues() {
    }
}
