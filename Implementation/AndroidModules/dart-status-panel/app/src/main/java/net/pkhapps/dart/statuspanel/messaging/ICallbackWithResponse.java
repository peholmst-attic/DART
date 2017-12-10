package net.pkhapps.dart.statuspanel.messaging;

import android.support.annotation.NonNull;

/**
 * Callback interface for operations that return a response upon successful completion.
 */
public interface ICallbackWithResponse<T> extends ICallback {

    /**
     * Called when a response has been successfully received.
     *
     * @param response the response.
     */
    void onSuccess(@NonNull T response);
}
