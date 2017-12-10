package net.pkhapps.dart.modules.base.rabbitmq;

import com.netflix.config.DynamicPropertyFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.function.Supplier;

/**
 * Default implementation of {@link RabbitMQProperties}. Feel free to subclass if you want to add
 * more properties.
 */
@ApplicationScoped
public class DefaultRabbitMQProperties implements RabbitMQProperties {

    private static final String RABBITMQ_RECONNECTION_DELAY_MS = "rabbitmq.reconnectionDelayMs";
    private static final String RABBITMQ_HOST = "rabbitmq.host";
    private static final String RABBITMQ_PORT = "rabbitmq.port";
    private static final String RABBITMQ_VIRTUAL_HOST = "rabbitmq.virtualHost";
    private static final String RABBITMQ_USERNAME = "rabbitmq.username";
    private static final String RABBITMQ_PASSWORD = "rabbitmq.password";
    private static final String RABBITMQ_CONNECTION_TIMEOUT_MS = "rabbitmq.connectionTimeoutMs";
    private static final String RABBITMQ_HANDSHAKE_TIMEOUT_MS = "rabbitmq.handshakeTimeoutMs";
    private static final String RABBITMQ_RPC_RESPONSE_TIMEOUT_MS = "rabbitmq.rpc.responseTimeoutMs";
    private static final String RABBITMQ_MESSAGE_EXPIRATION = "rabbitmq.message.expiration";
    private static final String RABBITMQ_MESSAGE_APP_ID = "rabbitmq.message.appId";


    @Override
    public Supplier<String> getHost() {
        return DynamicPropertyFactory.getInstance().getStringProperty(RABBITMQ_HOST, "localhost")::getValue;
    }

    @Override
    public Supplier<Integer> getPort() {
        return DynamicPropertyFactory.getInstance().getIntProperty(RABBITMQ_PORT, 5672)::getValue;
    }

    @Override
    public Supplier<String> getUsername() {
        return DynamicPropertyFactory.getInstance().getStringProperty(RABBITMQ_USERNAME, "guest")::getValue;
    }

    @Override
    public Supplier<String> getPassword() {
        return DynamicPropertyFactory.getInstance().getStringProperty(RABBITMQ_PASSWORD, "guest")::getValue;
    }

    @Override
    public Supplier<String> getVirtualHost() {
        return DynamicPropertyFactory.getInstance().getStringProperty(RABBITMQ_VIRTUAL_HOST, "")::getValue;
    }

    @Override
    public Supplier<Integer> getConnectionTimeoutMs() {
        return DynamicPropertyFactory.getInstance().getIntProperty(RABBITMQ_CONNECTION_TIMEOUT_MS, 5000)::getValue;
    }

    @Override
    public Supplier<Integer> getHandshakeTimeoutMs() {
        return DynamicPropertyFactory.getInstance().getIntProperty(RABBITMQ_HANDSHAKE_TIMEOUT_MS, 5000)::getValue;
    }

    @Override
    public Supplier<Integer> getReconnectionDelayMs() {
        return DynamicPropertyFactory.getInstance().getIntProperty(RABBITMQ_RECONNECTION_DELAY_MS, 5000)::getValue;
    }

    @Override
    public Supplier<Integer> getRpcResponseTimeoutMs() {
        return DynamicPropertyFactory.getInstance().getIntProperty(RABBITMQ_RPC_RESPONSE_TIMEOUT_MS, 5000)::getValue;
    }

    @Override
    public Supplier<String> getMessageExpiration() {
        return DynamicPropertyFactory.getInstance().getStringProperty(RABBITMQ_MESSAGE_EXPIRATION, "30000")::getValue;
    }

    @Override
    public Supplier<String> getMessageAppId() {
        return DynamicPropertyFactory.getInstance()
                .getStringProperty(RABBITMQ_MESSAGE_APP_ID, getClass().getPackage().getName())::getValue;
    }

}
