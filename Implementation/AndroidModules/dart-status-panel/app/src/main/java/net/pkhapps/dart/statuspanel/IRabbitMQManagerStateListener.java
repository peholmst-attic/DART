package net.pkhapps.dart.statuspanel;

import android.support.annotation.NonNull;

/**
 * Listener interface for receiving notifications when the state of a RabbitMQ manager changes.
 */
interface IRabbitMQManagerStateListener {

    /**
     * Invoked by the RabbitMQ manager when its state changes. Any exceptions thrown by this method will be ignored.
     *
     * @param rabbitMQManager the RabbitMQ manager that fired the event.
     * @param newState        the new state of the RabbitMQ manager.
     */
    void onStateChange(@NonNull RabbitMQManager rabbitMQManager, @NonNull RabbitMQManagerState newState);
}
