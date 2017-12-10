package net.pkhapps.dart.statuspanel.messaging;

import android.support.annotation.Nullable;

/**
 * Callback interface for asynchronous operations. An operation either completes successfully or
 * with an error, or times out.
 */
public interface ICallback {

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
