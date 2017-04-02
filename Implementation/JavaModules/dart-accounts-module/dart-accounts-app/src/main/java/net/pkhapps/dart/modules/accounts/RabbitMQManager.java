package net.pkhapps.dart.modules.accounts;

import com.netflix.config.DynamicPropertyFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownSignalException;
import net.pkhapps.dart.modules.accounts.event.RabbitMQConnectionClosed;
import net.pkhapps.dart.modules.accounts.event.RabbitMQConnectionOpened;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Manager that creates a connection to RabbitMQ and automatically attempts to reconnect if the connection is lost.
 */
@ApplicationScoped
class RabbitMQManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQManager.class);

    private static final String RABBITMQ_RECONNECTION_DELAY_MS = "rabbitmq.reconnectionDelayMs";
    private static final String RABBITMQ_HOST = "rabbitmq.host";
    private static final String RABBITMQ_PORT = "rabbitmq.port";
    private static final String RABBITMQ_VIRTUAL_HOST = "rabbitmq.virtualHost";
    private static final String RABBITMQ_USERNAME = "rabbitmq.username";
    private static final String RABBITMQ_PASSWORD = "rabbitmq.password";
    private static final String RABBITMQ_CONNECTION_TIMEOUT_MS = "rabbitmq.connectionTimeoutMs";
    private static final String RABBITMQ_HANDSHAKE_TIMEOUT_MS = "rabbitmq.handshakeTimeoutMs";

    @Inject
    Event<RabbitMQConnectionOpened> rabbitMQConnectionOpenedEvent;

    @Inject
    Event<RabbitMQConnectionClosed> rabbitMQConnectionClosedEvent;

    @Inject
    ScheduledExecutorService executorService;

    private Connection connection;

    void onContainerInitialized(@Observes ContainerInitialized containerInitialized) {
        connect();
    }

    private synchronized void connect() {
        LOGGER.info("Connecting to RabbitMQ");
        try {
            connection = createConnectionFactory().newConnection();
            connection.addShutdownListener(this::onShutdown);
            LOGGER.info("Connected to RabbitMQ using connection [{}]", connection);
            rabbitMQConnectionOpenedEvent.fire(new RabbitMQConnectionOpened(connection));
        } catch (Exception ex) {
            LOGGER.error("Error connecting to RabbitMQ", ex);
            scheduleReconnect();
        }
    }

    @PreDestroy
    private synchronized void disconnect() {
        if (connection != null) {
            try {
                LOGGER.info("Explicitly closing RabbitMQ connection [{}]", connection);
                connection.close();
            } catch (IOException ex) {
                LOGGER.error("Error closing RabbitMQ connection", ex);
            }
        }
    }

    private synchronized void onShutdown(ShutdownSignalException cause) {
        if (cause.isInitiatedByApplication()) {
            LOGGER.info("RabbitMQ connection closed by application");
        } else {
            LOGGER.warn("RabbitMQ connection closed by broker", cause);
        }
        try {
            rabbitMQConnectionClosedEvent.fire(new RabbitMQConnectionClosed(connection));
        } finally {
            connection = null;
            if (!cause.isInitiatedByApplication()) {
                scheduleReconnect();
            }
        }
    }

    private void scheduleReconnect() {
        final int delay = DynamicPropertyFactory.getInstance().
                getIntProperty(RABBITMQ_RECONNECTION_DELAY_MS, 5000).get();
        LOGGER.info("Scheduling reconnection attempt in {} ms", delay);
        executorService.schedule(this::connect, delay, TimeUnit.MILLISECONDS);
    }

    private ConnectionFactory createConnectionFactory() {
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        final DynamicPropertyFactory propertyFactory = DynamicPropertyFactory.getInstance();

        connectionFactory.setHost(propertyFactory.getStringProperty(RABBITMQ_HOST, "localhost").get());
        connectionFactory.setPort(propertyFactory.getIntProperty(RABBITMQ_PORT, 5672).get());
        connectionFactory.setUsername(propertyFactory.getStringProperty(RABBITMQ_USERNAME, "guest").get());
        connectionFactory.setPassword(propertyFactory.getStringProperty(RABBITMQ_PASSWORD, "guest").get());
        connectionFactory.setVirtualHost(propertyFactory.getStringProperty(RABBITMQ_VIRTUAL_HOST, "").get());

        connectionFactory
                .setConnectionTimeout(propertyFactory.getIntProperty(RABBITMQ_CONNECTION_TIMEOUT_MS, 1000).get());
        connectionFactory
                .setHandshakeTimeout(propertyFactory.getIntProperty(RABBITMQ_HANDSHAKE_TIMEOUT_MS, 1000).get());

        // We'll handle recovery ourselves
        connectionFactory.setAutomaticRecoveryEnabled(false);
        return connectionFactory;
    }
}
