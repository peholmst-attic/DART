package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer;

import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
public interface CallbackWithResponse<T> extends Callback {

    /**
     * Called when a response has been successfully received.
     *
     * @param response the response.
     */
    void onSuccess(@NotNull T response);
}
