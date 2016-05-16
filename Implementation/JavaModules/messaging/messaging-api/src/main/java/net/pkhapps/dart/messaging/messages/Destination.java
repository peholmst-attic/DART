package net.pkhapps.dart.messaging.messages;

public interface Destination {

    String getExchange();

    String getRoutingKey();
}
