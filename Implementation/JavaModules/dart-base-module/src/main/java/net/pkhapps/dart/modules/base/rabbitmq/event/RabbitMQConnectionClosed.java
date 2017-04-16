package net.pkhapps.dart.modules.base.rabbitmq.event;

import com.rabbitmq.client.Connection;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a RabbitMQ connection is closed, either explicitly or because of some error, such as the server
 * going down. Observers of this event should be <b>idempotent</b> and must <b>never</b> throw any exceptions.
 * <p>
 * Please note that classes that actually use RabbitMQ directly should prefer
 * {@link com.rabbitmq.client.ShutdownListener}s to this event.
 *
 * @see RabbitMQConnectionOpened
 */
public class RabbitMQConnectionClosed {

    private final Connection connection;

    public RabbitMQConnectionClosed(@NotNull Connection connection) {
        this.connection = connection;
    }

    /**
     * Returns the closed RabbitMQ connection.
     */
    @NotNull
    public Connection getConnection() {
        return connection;
    }
}
