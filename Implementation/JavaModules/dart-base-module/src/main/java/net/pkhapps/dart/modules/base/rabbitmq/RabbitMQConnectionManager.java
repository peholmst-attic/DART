package net.pkhapps.dart.modules.base.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownSignalException;
import net.pkhapps.dart.modules.base.rabbitmq.event.RabbitMQConnectionClosed;
import net.pkhapps.dart.modules.base.rabbitmq.event.RabbitMQConnectionOpened;
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
 * Manager that creates a connection to RabbitMQ as soon as the WELD container is
 * {@link ContainerInitialized initialized} and automatically attempts to reconnect if the connection is lost. It
 * fires a {@link RabbitMQConnectionOpened} event when the connection is established and a
 * {@link RabbitMQConnectionClosed} event when the connection is closed. However, it is recommended for modules that
 * use RabbitMQ to use {@link com.rabbitmq.client.ShutdownListener}s to detect when a connection or channel goes down.
 *
 * @see RabbitMQChannelManager
 */
@ApplicationScoped
class RabbitMQConnectionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConnectionManager.class);

    @Inject
    RabbitMQProperties rabbitMQProperties;

    @Inject
    Event<RabbitMQConnectionOpened> rabbitMQConnectionOpenedEvent;

    @Inject
    Event<RabbitMQConnectionClosed> rabbitMQConnectionClosedEvent;

    @Inject
    ScheduledExecutorService executorService;

    private Connection connection;

    private void onContainerInitialized(@Observes ContainerInitialized containerInitialized) {
        connect();
    }

    private synchronized void connect() {
        LOGGER.info("Connecting to RabbitMQ");
        try {
            if (connection == null) {
                connection = createConnectionFactory().newConnection();
                connection.addShutdownListener(this::onShutdown);
                LOGGER.info("Connected to RabbitMQ using connection [{}]", connection);
            } else {
                LOGGER.info("Already connected to RabbitMQ using connection [{}]", connection);
            }
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
            LOGGER.warn("RabbitMQ connection closed by broker: {}", cause.getMessage());
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
        final int delay = rabbitMQProperties.getReconnectionDelayMs().get();
        LOGGER.info("Scheduling reconnection attempt in {} ms", delay);
        executorService.schedule(this::connect, delay, TimeUnit.MILLISECONDS);
    }

    private ConnectionFactory createConnectionFactory() {
        final ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost(rabbitMQProperties.getHost().get());
        connectionFactory.setPort(rabbitMQProperties.getPort().get());
        connectionFactory.setUsername(rabbitMQProperties.getUsername().get());
        connectionFactory.setPassword(rabbitMQProperties.getPassword().get());
        connectionFactory.setVirtualHost(rabbitMQProperties.getVirtualHost().get());

        connectionFactory.setConnectionTimeout(rabbitMQProperties.getConnectionTimeoutMs().get());
        connectionFactory.setHandshakeTimeout(rabbitMQProperties.getHandshakeTimeoutMs().get());

        // We'll handle recovery ourselves
        connectionFactory.setAutomaticRecoveryEnabled(false);
        return connectionFactory;
    }
}
