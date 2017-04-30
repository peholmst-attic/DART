package net.pkhapps.dart.modules.base.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ShutdownSignalException;
import net.pkhapps.dart.modules.base.rabbitmq.event.RabbitMQConnectionOpened;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Base class for beans that set up and use a RabbitMQ channel. An example could be a bean that sets up a message
 * consumer for a queue. This manager will automatically track the state of the channel and automatically re-setup
 * when the channel goes down.
 * <p>
 * Remember to make sure your subclass is picked up by CDI (e.g. by annotating it with
 * {@link javax.enterprise.context.ApplicationScoped}).
 *
 * @see #setUp(Channel)
 */
public abstract class RabbitMQChannelManager {

    /**
     * Shared protected logger, may be used by subclasses.
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Channel channel;

    protected final ScheduledExecutorService executorService;
    protected final RabbitMQProperties rabbitMQProperties;

    @Inject
    public RabbitMQChannelManager(ScheduledExecutorService executorService, RabbitMQProperties rabbitMQProperties) {
        this.executorService = executorService;
        this.rabbitMQProperties = rabbitMQProperties;
    }

    // No need to react to the RabbitMQConnectionClosed event since we do that through the shutdown listener.

    private boolean channelClosedBecauseOfSetupError;

    public void onRabbitMQConnectionOpened(@Observes RabbitMQConnectionOpened connectionOpened) throws
            IOException {
        setUpChannel(connectionOpened.getConnection());
    }

    private synchronized void setUpChannel(Connection connection) {
        channelClosedBecauseOfSetupError = false;
        logger.info("Setting up RabbitMQ channel for connection [{}]", connection);
        try {
            if (channel == null || !channel.isOpen()) {
                channel = connection.createChannel();
                channel.addShutdownListener(this::onShutdown);
                logger.info("Created RabbitMQ channel [{}]", channel);
            }

            channel.confirmSelect();
            channel.basicQos(1);

            setUp(channel);
        } catch (Exception ex) {
            if (connection.isOpen() && channel.isOpen()) {
                logger.error("Error during setup, closing channel", ex);
                try {
                    channelClosedBecauseOfSetupError = true;
                    channel.close();
                } catch (Exception ex2) {
                    // Ignore it.
                }
            } else {
                logger.error("Error during setup", ex);
            }
        }
    }

    private synchronized void onShutdown(ShutdownSignalException cause) {
        if (cause.isInitiatedByApplication()) {
            if (channelClosedBecauseOfSetupError) {
                logger.warn("RabbitMQ channel was closed because of a setup error");
            } else {
                logger.info("RabbitMQ channel was closed by application");
            }
        } else {
            logger.warn("RabbitMQ channel was closed by broker: {}", cause.getMessage());
        }
        try {
            tearDown(channel);
        } catch (Exception ex) {
            logger.error("Error during teardown", ex);
        } finally {
            Connection connection = channel.getConnection();
            channel = null;
            if (connection.isOpen() && channelClosedBecauseOfSetupError || !cause.isInitiatedByApplication()) {
                // Connection is still open, so the problem was in the channel
                scheduleRetry(connection);
            }
        }
    }

    /**
     * Called by the manager to set up the RabbitMQ channel, e.g. register any message consumers. When this method is
     * called, the channel has already been created, {@link Channel#confirmSelect()} has been called and
     * {@link Channel#basicQos(int)} has been set to 1.
     * If this method throws any exceptions, the manager will automatically close the channel if it is still open and
     * retry in {@link RabbitMQProperties#getReconnectionDelayMs()} milliseconds.
     *
     * @param channel the RabbitMQ channel to use.
     * @throws Exception if something goes wrong during the set up.
     */
    protected abstract void setUp(Channel channel) throws Exception;

    /**
     * Called by the manager after the RabbitMQ channel has been closed. In most cases, you don't need to implement
     * this method which is why it has a default empty implementation.
     *
     * @param channel the closed RabbitMQ channel.
     */
    protected void tearDown(Channel channel) throws Exception {
    }

    private void scheduleRetry(Connection connection) {
        final int delay = rabbitMQProperties.getReconnectionDelayMs().get();
        logger.info("Scheduling new setup attempt in {} ms", delay);
        executorService.schedule(() -> setUpChannel(connection), delay, TimeUnit.MILLISECONDS);
    }
}

