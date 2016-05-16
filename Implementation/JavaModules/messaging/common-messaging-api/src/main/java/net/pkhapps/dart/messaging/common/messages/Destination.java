package net.pkhapps.dart.messaging.common.messages;

public interface Destination {

    String getExchange();

    String getRoutingKey();
}
