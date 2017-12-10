package net.pkhapps.dart.statuspanel.messaging;

/**
 * Callback interface for operations that don't return a response, only a success or error
 * notification.
 */
public interface ICallbackWithoutResponse extends ICallback {

    /**
     * Called when the operation has successfully completed.
     */
    void onSuccess();
}
