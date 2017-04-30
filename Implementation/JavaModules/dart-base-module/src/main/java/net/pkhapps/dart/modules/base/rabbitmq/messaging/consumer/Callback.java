package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer;

import org.jetbrains.annotations.Nullable;

/**
 * TODO Document me!
 */
public interface Callback {

    /**
     * Called when no response has been received within the time limit.
     */
    void onTimeout();

    /**
     * Called when an error response has been received.
     *
     * @param errorCode    the error code.
     * @param errorMessage an optional message describing the error.
     */
    void onError(int errorCode, @Nullable String errorMessage);
}
