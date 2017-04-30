package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer;

/**
 * TODO Document me!
 */
public interface CallbackWithoutResponse extends Callback {

    /**
     * Called when the operation has successfully completed.
     */
    void onSuccess();
}
