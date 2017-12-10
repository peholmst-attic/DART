package net.pkhapps.dart.statuspanel;

import android.support.annotation.NonNull;
import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

import net.pkhapps.dart.statuspanel.messaging.DefaultMessageBroker;
import net.pkhapps.dart.statuspanel.messaging.IMessageBroker;

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
    private static final int RECOVER_INTERVAL_MS = 5000;
    private static final int CONNECTION_TIMEOUT_MS = 1000;
    private static final int HANDSHAKE_TIMEOUT_MS = 1000;

    private final DefaultMessageBroker messageBroker = new DefaultMessageBroker();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ScheduledExecutorService executorService;
    private Connection connection;
    private Channel channel;
    private RabbitMQManagerState state = RabbitMQManagerState.DISCONNECTED;
    private final Set<IRabbitMQManagerStateListener> stateListeners = new HashSet<>();
    private ScheduledFuture<?> recoveryJob;

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
     * @param job
     */
    void doWithMessageBroker(final IMessageBrokerJob job) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                job.doWithMessageBroker(messageBroker);
            }
        });
    }

    /**
     * TODO Document me!
     */
    interface IMessageBrokerJob {

        /**
         *
         * @param messageBroker
         */
        void doWithMessageBroker(IMessageBroker messageBroker);
    }

    private void doConnect() {
        lock.writeLock().lock();
        try {
            if (connection == null) {
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
                    messageBroker.setChannel(channel);
                    Log.i(TAG, "Successfully opened RabbitMQ channel");

                    setStateWithoutLock(RabbitMQManagerState.CONNECTED);
                } catch (Exception ex) {
                    Log.e(TAG, "Error connecting to RabbitMQ", ex);
                    scheduleReconnectWithoutLock();
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
            cancelConnectionRetryWithoutLock();
            if (connection != null) {
                Log.i(TAG, "Disconnecting from RabbitMQ server");
                setStateWithoutLock(RabbitMQManagerState.DISCONNECTING);
                connection.close();
                // The rest of the clean up work will take place in the shutdown listener.
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error closing RabbitMQ connection", ex);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void onShutdownCompleted(ShutdownSignalException cause) {
        lock.writeLock().lock();
        try {
            if (cause.isInitiatedByApplication()) {
                Log.i(TAG, "RabbitMQ connection closed by application");
            } else {
                Log.w(TAG, "RabbitMQ connection closed by broker", cause);
            }
            channel = null;
            connection = null;
            setStateWithoutLock(RabbitMQManagerState.DISCONNECTED);
        } finally {
            if (!cause.isInitiatedByApplication()) {
                scheduleReconnectWithoutLock();
            }
            lock.writeLock().unlock();
        }
    }

    private void scheduleReconnectWithoutLock() {
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
