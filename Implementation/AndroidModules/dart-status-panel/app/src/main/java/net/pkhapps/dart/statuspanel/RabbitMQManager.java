package net.pkhapps.dart.statuspanel;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

import net.pkhapps.dart.statuspanel.json.JsonMessage;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Helper class for managing the connection to the RabbitMQ server.
 */
class RabbitMQManager {

    private static final String TAG = "RabbitMQManager";
    private static final String MESSAGE_APP_ID = "android-status-panel";
    private static final int RECOVER_INTERVAL_MS = 5000;
    private static final int CONNECTION_TIMEOUT_MS = 1000;
    private static final int HANDSHAKE_TIMEOUT_MS = 1000;
    private static final String EXCHANGE = "";
    private static final String MESSAGE_EXPIRATION = "10000"; // 10 seconds

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ScheduledExecutorService executorService;
    private Connection connection;
    private Channel channel;
    private RabbitMQManagerState state = RabbitMQManagerState.DISCONNECTED;
    private final Set<IRabbitMQManagerStateListener> stateListeners = new HashSet<>();
    private ScheduledFuture<?> recoveryJob;
    private final Set<String> declaredQueues = new HashSet<>();

    /**
     * Initializes the RabbitMQ manager. Call this method when the owning service is created.
     */
    void init() {
        Log.i(TAG, "Initializing RabbitMQ manager");
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Destroys the RabbitMQ manager. Call this method when the owning service is destroyed. If the
     * RabbitMQ manager is connected to the RabbitMQ server, it will be disconnected.
     */
    void destroy() {
        Log.i(TAG, "Destroying RabbitMQ manager");
        disconnect(); // Disconnect if there is a connection
        executorService.shutdown();
    }

    /**
     * Attempts to connect to the RabbitMQ server. If it does not work, the RabbitMQ manager will
     * automatically retry until the connection is established or {@link #disconnect()} is called.
     */
    void connect() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                doConnect();
            }
        });
    }

    /**
     * TODO Document me!
     *
     * @param queueName
     * @param properties
     * @param body
     */
    void publishToQueue(final String queueName, final AMQP.BasicProperties properties,
                        final JsonMessage body) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                doPublishToQueue(queueName, properties, body);
            }
        });
    }

    private void doPublishToQueue(String queueName, AMQP.BasicProperties properties, JsonMessage body) {
        lock.readLock().lock();
        try {
            if (state == RabbitMQManagerState.CONNECTED) {
                if (!declaredQueues.contains(queueName)) {
                    channel.queueDeclare(queueName, false, false, false, null);
                    declaredQueues.add(queueName);
                }
                AMQP.BasicProperties.Builder builder;
                if (properties == null) {
                    builder = new AMQP.BasicProperties.Builder();
                } else {
                    builder = properties.builder();
                }
                builder
                        .userId("pettersandroid") // TODO Read from some preference storage
                        .appId(MESSAGE_APP_ID)
                        .type("json-message")
                        .contentType("application/json")
                        .contentEncoding("UTF-8")
                        .expiration(MESSAGE_EXPIRATION)
                        .timestamp(new Date());
                channel.basicPublish(EXCHANGE, queueName, builder.build(),
                        objectMapper.writeValueAsBytes(body));
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error while publishing to queue", ex);
        } finally {
            lock.readLock().unlock();
        }
    }

    private void doConnect() {
        lock.writeLock().lock();
        try {
            if (state.isOkToAttemptConnection()) {
                cancelConnectionRetryWithoutLock();
                Log.i(TAG, "Connecting to RabbitMQ server");
                setStateWithoutLock(RabbitMQManagerState.CONNECTING);
                final ConnectionFactory connectionFactory = new ConnectionFactory();

                // TODO Read from some preference storage
                connectionFactory.setHost("192.168.1.121");
                connectionFactory.setUsername("pettersandroid");
                connectionFactory.setPassword("password");
                connectionFactory.setVirtualHost("dart");

                // We'll handle recovery manually
                connectionFactory.setAutomaticRecoveryEnabled(false);

                connectionFactory.setConnectionTimeout(CONNECTION_TIMEOUT_MS);
                connectionFactory.setHandshakeTimeout(HANDSHAKE_TIMEOUT_MS);

                try {
                    connection = connectionFactory.newConnection();
                    connection.addShutdownListener(new ShutdownListener() {
                        @Override
                        public void shutdownCompleted(ShutdownSignalException cause) {
                            onShutdownCompleted(cause);
                        }
                    });
                    Log.i(TAG, "Successfully opened RabbitMQ connection");

                    channel = connection.createChannel();
                    channel.confirmSelect();
                    channel.basicQos(1);
                    Log.i(TAG, "Successfully opened RabbitMQ channel");

                    setStateWithoutLock(RabbitMQManagerState.CONNECTED);
                } catch (Exception ex) {
                    Log.e(TAG, "Error connecting to RabbitMQ server", ex);
                    closeConnectionsWithoutLock();
                    scheduleConnectionRetryWithoutLock();
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Disconnects from the RabbitMQ server. If the RabbitMQ manager is not connected or attempting
     * to connect, nothing happens.
     */
    void disconnect() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                doDisconnect();
            }
        });
    }

    private void doDisconnect() {
        lock.writeLock().lock();
        try {
            if (state == RabbitMQManagerState.CONNECTED) {
                Log.i(TAG, "Disconnecting from RabbitMQ server");
                setStateWithoutLock(RabbitMQManagerState.DISCONNECTING);
                closeConnectionsWithoutLock();
            } else {
                cancelConnectionRetryWithoutLock();
                setStateWithoutLock(RabbitMQManagerState.DISCONNECTED);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void closeConnectionsWithoutLock() {
        try {
            if (channel != null) {
                channel.close();
                Log.i(TAG, "Successfully closed RabbitMQ channel");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error closing RabbitMQ channel", ex);
        } finally {
            channel = null;
        }
        try {
            if (connection != null) {
                connection.close();
                Log.i(TAG, "Successfully closed RabbitMQ connection");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error closing RabbitMQ connection", ex);
        } finally {
            connection = null;
        }
    }

    private void onShutdownCompleted(ShutdownSignalException cause) {
        lock.writeLock().lock();
        try {
            Log.i(TAG, "RabbitMQ connection shut down completed");
            // When explicitly disconnecting, the state will be DISCONNECTING
            final boolean needToRecover = (state == RabbitMQManagerState.CONNECTED);
            channel = null;
            connection = null;
            declaredQueues.clear();
            if (needToRecover) {
                setStateWithoutLock(RabbitMQManagerState.CONNECTING);
                scheduleConnectionRetryWithoutLock();
            } else {
                setStateWithoutLock(RabbitMQManagerState.DISCONNECTED);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void scheduleConnectionRetryWithoutLock() {
        if (recoveryJob == null || recoveryJob.isDone() || recoveryJob.isCancelled()) {
            Log.i(TAG, "Scheduling connection retry attempt");
            recoveryJob = executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    doConnect();
                }
            }, RECOVER_INTERVAL_MS, TimeUnit.MILLISECONDS);
        }
    }

    private void cancelConnectionRetryWithoutLock() {
        if (recoveryJob != null && !recoveryJob.isDone() && !recoveryJob.isCancelled()) {
            Log.i(TAG, "Canceling connection retry attempt");
            recoveryJob.cancel(true);
            recoveryJob = null;
        }
    }

    /**
     * Returns whether the RabbitMQ manager is currently connected to the RabbitMQ server or not.
     */
    boolean isConnected() {
        return getState() == RabbitMQManagerState.CONNECTED;
    }

    /**
     * Returns the current state of the RabbitMQ manager.
     */
    @NonNull
    RabbitMQManagerState getState() {
        lock.readLock().lock();
        try {
            return state;
        } finally {
            lock.readLock().unlock();
        }
    }

    private void setStateWithoutLock(@NonNull RabbitMQManagerState state) {
        if (this.state != state) {
            this.state = state;
            if (Log.isLoggable(TAG, Log.INFO)) {
                Log.i(TAG, "State of RabbitMQ manager is now " + state);
            }
            notifyStateListenersWithoutLock(state);
        }
    }

    private void notifyStateListenersWithoutLock(@NonNull RabbitMQManagerState state) {
        for (IRabbitMQManagerStateListener listener : new HashSet<>(stateListeners)) {
            try {
                listener.onStateChange(this, state);
            } catch (Exception ex) {
                // Log and ignore
                Log.e(TAG, "Exception thrown by state listener", ex);
            }
        }
    }

    /**
     * Adds the specified listener to be notified when the state of the RabbitMQ manager changes.
     */
    void addStateListener(@NonNull IRabbitMQManagerStateListener stateListener) {
        lock.writeLock().lock();
        try {
            stateListeners.add(stateListener);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Removes the specified listener previously added by
     * {@link #addStateListener(IRabbitMQManagerStateListener)}.
     */
    void removeStateListener(@NonNull IRabbitMQManagerStateListener stateListener) {
        lock.writeLock().lock();
        try {
            stateListeners.remove(stateListener);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
