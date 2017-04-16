package net.pkhapps.dart.modules.base.rabbitmq.event;

import com.rabbitmq.client.Connection;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a RabbitMQ connection is established. Observers of this event must be <b>idempotent</b> and
 * should throw an exception if an error occurs that the event handler cannot recover from. This event will be
 * re-fired until all observers complete without errors, which is why observers must be prepared to receive an event
 * for the same connection several times.
 *
 * @see RabbitMQConnectionClosed
 */
public class RabbitMQConnectionOpened {

    private final Connection connection;

    public RabbitMQConnectionOpened(@NotNull Connection connection) {
        this.connection = connection;
    }

    /**
     * Return the opened RabbitMQ connection.
     */
    @NotNull
    public Connection getConnection() {
        return connection;
    }
}
