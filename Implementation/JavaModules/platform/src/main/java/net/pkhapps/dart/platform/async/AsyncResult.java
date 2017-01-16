package net.pkhapps.dart.platform.async;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Interface representing the result of an asynchronous job. The job can either complete successfully with a result,
 * fail with an exception or time out. {@link Observer}s can be subscribed to the result and get notified whenever the
 * state changes.
 * <p>
 * There are many existing libraries that provide features like this, such as Guava or RxJava. I chose to write my own
 * implementation for learning purposes only - I wanted to find out what is needed to implement something like this
 * from scratch.
 *
 * @param <V> the type of the result produced by the background job.
 */
public interface AsyncResult<V> {

    /**
     * Subscribes the given {@code observer} to be notified when the state of the job changes.
     *
     * @param observer the observer to subscribe.
     */
    void subscribe(@NotNull Observer<V> observer);

    /**
     * Returns whether the job has completed, regardless of the end result.
     *
     * @return true if the job has completed, false if it is still running.
     */
    boolean isDone();

    /**
     * Returns whether the job completed successfully.
     *
     * @return true if the job completed successfully, false if it is still running, timed out or threw an exception.
     */
    boolean isSuccessful();

    /**
     * Returns whether the job timed out while waiting for the result.
     *
     * @return true if the job timed out, false if it is still running or completed in time.
     */
    boolean isTimedOut();

    /**
     * Returns the successful result of the job.
     *
     * @return the result, or an empty {@code Optional} if the job is still running, returned {@code null},
     * timed out or threw an exception.
     */
    @NotNull
    Optional<V> getResult();

    /**
     * Returns the exception thrown by the job.
     *
     * @return the exception, or an empty {@code Optional} if the operation is still running or did not throw any
     * exception.
     */
    @NotNull
    Optional<Exception> getError();
}
