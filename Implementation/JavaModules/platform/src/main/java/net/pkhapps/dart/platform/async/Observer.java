package net.pkhapps.dart.platform.async;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for observers that want to subscribe to changes of an {@link AsyncResult}. Any exceptions thrown by any
 * of the methods in this interface will be silently ignored.
 *
 * @param <V> the type of the result produced by the background job.
 */
public interface Observer<V> {

    /**
     * Called when the background job of an {@link AsyncResult} times out.
     *
     * @see AsyncResult#isTimedOut()
     */
    void onTimeOut();

    /**
     * Called when the background job of an {@link AsyncResult} throws an exception.
     *
     * @param error the error thrown by the job.
     * @see AsyncResult#getError()
     */
    void onError(@NotNull Exception error);

    /**
     * Called when the backend job of an {@link AsyncResult} completes successfully.
     *
     * @param result the result produced by the job.
     * @see AsyncResult#getResult()
     * @see AsyncResult#isSuccessful()
     */
    void onSuccess(@Nullable V result);
}
