package net.pkhapps.dart.platform.async;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Default implementation of {@link AsyncResult}. This implementation does not wrap a
 * {@link java.util.concurrent.Callable}. Instead it is the duty of the creator to invoke either
 * {@link #success(Object)} or {@link #error(Exception)} to notify the result of the outcome of the job. If none of
 * these methods are invoked within a given amount of time, the result will time out. If the creator needs to take any
 * special action when this happens (such as cancel some background job), it can subscribe to the result itself.
 */
public class DefaultAsyncResult<V> implements AsyncResult<V> {

    private final ScheduledExecutorService executorService;
    private final Set<Observer<V>> observers = new HashSet<>();
    private final ScheduledFuture<?> timeOutJob;
    private boolean done = false;
    private boolean timedOut = false;
    private Exception error = null;
    private V result = null;

    /**
     * Creates a new {@code DefaultAsyncResult}. The creator may use {@link #success(Object)} and
     * {@link #error(Exception)} to report the outcome of the job.
     *
     * @param executorService the executorService service to use.
     * @param timeOutMs       the time out of the job in milliseconds.
     */
    public DefaultAsyncResult(@NotNull ScheduledExecutorService executorService, long timeOutMs) {
        this.executorService = Objects.requireNonNull(executorService, "executorService must not be null");
        timeOutJob = executorService.schedule(this::timeOut, timeOutMs, TimeUnit.MILLISECONDS);
    }

    private void timeOut() {
        synchronized (this) {
            if (done) {
                return;
            }
            timeOutJob.cancel(true);
            done = true;
            timedOut = true;
        }
        notifyObservers(Observer::onTimeOut);
    }

    /**
     * Completes the job successfully. If the job is already done, nothing happens.
     *
     * @param result the result of the job.
     */
    public void success(@Nullable V result) {
        synchronized (this) {
            if (done) {
                return;
            }
            timeOutJob.cancel(true);
            done = true;
            this.result = result;
        }
        notifyObservers(o -> o.onSuccess(result));
    }

    /**
     * Completes the job with an error. If the job is already done, nothing happens.
     *
     * @param error the error.
     */
    public void error(@NotNull Exception error) {
        Objects.requireNonNull(error, "error must not be null");
        synchronized (this) {
            if (done) {
                return;
            }
            timeOutJob.cancel(true);
            done = true;
            this.error = error;
        }
        notifyObservers(o -> o.onError(error));
    }

    private void notifyObservers(@NotNull Consumer<Observer<V>> action) {
        Set<Observer<V>> copyOfObservers;
        synchronized (observers) {
            copyOfObservers = new HashSet<>(observers);
        }
        executorService.submit(() -> copyOfObservers.forEach(o -> {
            try {
                action.accept(o);
            } catch (Exception ex) {
                // Silently ignore
            }
        }));
    }

    @Override
    public void subscribe(@NotNull Observer<V> observer) {
        Objects.requireNonNull(observer, "observer must not be null");
        synchronized (observers) {
            observers.add(observer);
        }
    }

    @Override
    public synchronized boolean isDone() {
        return done;
    }

    @Override
    public synchronized boolean isSuccessful() {
        return done && !timedOut && error == null;
    }

    @Override
    public synchronized boolean isTimedOut() {
        return timedOut;
    }

    @NotNull
    @Override
    public synchronized Optional<V> getResult() {
        return Optional.ofNullable(result);
    }

    @NotNull
    @Override
    public synchronized Optional<Exception> getError() {
        return Optional.ofNullable(error);
    }
}
