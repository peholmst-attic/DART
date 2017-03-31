package net.pkhapps.dart.statuspanel;

/**
 * Enumeration of the different states of the RabbitMQ manager.
 */
enum RabbitMQManagerState {
    /**
     * The manager is connecting to the RabbitMQ server.
     */
    CONNECTING(true),
    /**
     * The manager is connected to the RabbitMQ server.
     */
    CONNECTED(false),
    /**
     * The manager is currently being explicitly disconnected.
     */
    DISCONNECTING(false),
    /**
     * The manager is disconnected from the RabbitMQ server.
     */
    DISCONNECTED(true),
    /**
     * The manager can't connect to the RabbitMQ server because the connection parameters are invalid.
     */
    INVALID_CONNECTION_PARAMETERS(false);

    final boolean okToAttemptConnection;

    RabbitMQManagerState(boolean okToAttemptConnection) {
        this.okToAttemptConnection = okToAttemptConnection;
    }

    /**
     * Returns whether the RabbitMQ manager may attempt to connect to the RabbitMQ server in this state.
     */
    boolean isOkToAttemptConnection() {
        return okToAttemptConnection;
    }
}
