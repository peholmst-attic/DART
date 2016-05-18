package net.pkhapps.dart.messaging.amqp.routing;

import net.pkhapps.dart.messaging.routing.Destination;

public class AmqpDestination implements Destination {

    private final String exchange;
    private final String routingKey;

    public AmqpDestination(String exchange, String routingKey) {
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public String getExchange() {
        return exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}
