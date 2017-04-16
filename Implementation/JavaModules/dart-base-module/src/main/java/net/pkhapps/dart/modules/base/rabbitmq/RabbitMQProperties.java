package net.pkhapps.dart.modules.base.rabbitmq;

import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

/**
 * Bean for getting easy access to the RabbitMQ configuration properties. Feel free to subclass if you want to add
 * more properties (this bean is annotated as an {@link Alternative} so it will not conflict with the subclass bean).
 */
@ApplicationScoped
@Alternative
public class RabbitMQProperties {

    private static final String RABBITMQ_RECONNECTION_DELAY_MS = "rabbitmq.reconnectionDelayMs";
    private static final String RABBITMQ_HOST = "rabbitmq.host";
    private static final String RABBITMQ_PORT = "rabbitmq.port";
    private static final String RABBITMQ_VIRTUAL_HOST = "rabbitmq.virtualHost";
    private static final String RABBITMQ_USERNAME = "rabbitmq.username";
    private static final String RABBITMQ_PASSWORD = "rabbitmq.password";
    private static final String RABBITMQ_CONNECTION_TIMEOUT_MS = "rabbitmq.connectionTimeoutMs";
    private static final String RABBITMQ_HANDSHAKE_TIMEOUT_MS = "rabbitmq.handshakeTimeoutMs";

    /**
     * The host of the RabbitMQ server.
     */
    public DynamicStringProperty getHost() {
        return DynamicPropertyFactory.getInstance().getStringProperty(RABBITMQ_HOST, "localhost");
    }

    /**
     * The port of the RabbitMQ server.
     */
    public DynamicIntProperty getPort() {
        return DynamicPropertyFactory.getInstance().getIntProperty(RABBITMQ_PORT, 5672);
    }

    /**
     * The username to use when connecting to the RabbitMQ server.
     */
    public DynamicStringProperty getUsername() {
        return DynamicPropertyFactory.getInstance().getStringProperty(RABBITMQ_USERNAME, "guest");
    }

    /**
     * The password to use when connecting to the RabbitMQ server.
     */
    public DynamicStringProperty getPassword() {
        return DynamicPropertyFactory.getInstance().getStringProperty(RABBITMQ_PASSWORD, "guest");
    }

    /**
     * The virtual host to connect to.
     */
    public DynamicStringProperty getVirtualHost() {
        return DynamicPropertyFactory.getInstance().getStringProperty(RABBITMQ_VIRTUAL_HOST, "");
    }

    /**
     * The connection timeout in milliseconds.
     */
    public DynamicIntProperty getConnectionTimeoutMs() {
        return DynamicPropertyFactory.getInstance().getIntProperty(RABBITMQ_CONNECTION_TIMEOUT_MS, 1000);
    }

    /**
     * The handshake timeout in milliseconds.
     */
    public DynamicIntProperty getHandshakeTimeoutMs() {
        return DynamicPropertyFactory.getInstance().getIntProperty(RABBITMQ_HANDSHAKE_TIMEOUT_MS, 1000);
    }

    /**
     * The number of milliseconds to wait before retrying after a connection failure.
     */
    public DynamicIntProperty getReconnectionDelayMs() {
        return DynamicPropertyFactory.getInstance().getIntProperty(RABBITMQ_RECONNECTION_DELAY_MS, 1000);
    }
}
