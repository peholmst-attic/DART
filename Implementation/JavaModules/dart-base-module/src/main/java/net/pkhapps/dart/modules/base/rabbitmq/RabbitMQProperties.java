package net.pkhapps.dart.modules.base.rabbitmq;

import java.util.function.Supplier;

/**
 * Interface for getting easy access to the RabbitMQ configuration properties. Feel free to subclass if you want to add
 * more properties.
 */
public interface RabbitMQProperties {

    /**
     * The host of the RabbitMQ server.
     */
    Supplier<String> getHost();

    /**
     * The port of the RabbitMQ server.
     */
    Supplier<Integer> getPort();

    /**
     * The username to use when connecting to the RabbitMQ server.
     */
    Supplier<String> getUsername();

    /**
     * The password to use when connecting to the RabbitMQ server.
     */
    Supplier<String> getPassword();

    /**
     * The virtual host to connect to.
     */
    Supplier<String> getVirtualHost();

    /**
     * The connection timeout in milliseconds.
     */
    Supplier<Integer> getConnectionTimeoutMs();

    /**
     * The handshake timeout in milliseconds.
     */
    Supplier<Integer> getHandshakeTimeoutMs();

    /**
     * The number of milliseconds to wait before retrying after a connection failure.
     */
    Supplier<Integer> getReconnectionDelayMs();

    /**
     * The number of milliseconds to wait for a response when performing an asynchronous RPC call over RabbitMQ.
     */
    Supplier<Integer> getRpcResponseTimeoutMs();
}
