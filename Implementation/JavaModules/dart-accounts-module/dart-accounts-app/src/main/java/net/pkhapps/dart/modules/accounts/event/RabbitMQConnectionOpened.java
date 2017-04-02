package net.pkhapps.dart.modules.accounts.event;

import com.rabbitmq.client.Connection;
import org.jetbrains.annotations.NotNull;

/**
 * Created by peholmst on 02/04/2017.
 */
public class RabbitMQConnectionOpened {

    private final Connection connection;

    public RabbitMQConnectionOpened(@NotNull Connection connection) {
        this.connection = connection;
    }

    @NotNull
    public Connection getConnection() {
        return connection;
    }
}
