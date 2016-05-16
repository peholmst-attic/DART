package net.pkhapps.dart.messaging.routing;

public interface Destination {

    String getExchange();

    String getRoutingKey();
}
