package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * TODO Document me
 */
@ApplicationScoped
public class DefaultSyncMessageBroker implements SyncMessageBroker {

    private final AsyncMessageBroker asyncMessageBroker;

    @Inject
    public DefaultSyncMessageBroker(AsyncMessageBroker asyncMessageBroker) {
        this.asyncMessageBroker = asyncMessageBroker;
    }

    @Override
    public <REQUEST, RESPONSE> RESPONSE sendRequest(@NotNull REQUEST request,
                                                    @NotNull OutgoingMessageConverter<REQUEST> requestConverter,
                                                    @NotNull IncomingMessageConverter<RESPONSE> responseConverter)
            throws IOException, ApplicationException, TimeoutException {
        final AtomicReference<RESPONSE> responseContainer = new AtomicReference<>();
        final AtomicBoolean timedOut = new AtomicBoolean(false);
        final AtomicReference<ApplicationException> errorContainer = new AtomicReference<>();
        final CallbackWithResponse<RESPONSE> callback = new CallbackWithResponse<RESPONSE>() {
            @Override
            public void onSuccess(@NotNull RESPONSE response) {
                synchronized (DefaultSyncMessageBroker.this) {
                    responseContainer.set(response);
                    DefaultSyncMessageBroker.this.notifyAll();
                }
            }

            @Override
            public void onTimeout() {
                synchronized (DefaultSyncMessageBroker.this) {
                    timedOut.set(true);
                    DefaultSyncMessageBroker.this.notifyAll();
                }
            }

            @Override
            public void onError(int errorCode, @Nullable String errorMessage) {
                synchronized (DefaultSyncMessageBroker.this) {
                    errorContainer.set(new ApplicationException(errorMessage, errorCode));
                    DefaultSyncMessageBroker.this.notifyAll();
                }
            }
        };
        asyncMessageBroker.sendRequest(request, callback, requestConverter, responseConverter);
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new IOException("The thread was interrupted while waiting for a response", ex);
            }
        }
        if (timedOut.get()) {
            throw new TimeoutException();
        }
        final ApplicationException error = errorContainer.get();
        if (error != null) {
            throw error;
        }
        return responseContainer.get();
    }

    @Override
    public <COMMAND> void sendCommand(@NotNull COMMAND command,
                                      @NotNull OutgoingMessageConverter<COMMAND> commandConverter)
            throws IOException, ApplicationException, TimeoutException {
        final AtomicBoolean timedOut = new AtomicBoolean(false);
        final AtomicReference<ApplicationException> errorContainer = new AtomicReference<>();
        final CallbackWithoutResponse callback = new CallbackWithoutResponse() {
            @Override
            public void onSuccess() {
                synchronized (DefaultSyncMessageBroker.this) {
                    DefaultSyncMessageBroker.this.notifyAll();
                }
            }

            @Override
            public void onTimeout() {
                synchronized (DefaultSyncMessageBroker.this) {
                    timedOut.set(true);
                    DefaultSyncMessageBroker.this.notifyAll();
                }
            }

            @Override
            public void onError(int errorCode, @Nullable String errorMessage) {
                synchronized (DefaultSyncMessageBroker.this) {
                    errorContainer.set(new ApplicationException(errorMessage, errorCode));
                    DefaultSyncMessageBroker.this.notifyAll();
                }
            }
        };
        asyncMessageBroker.sendCommand(command, callback, commandConverter);
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new IOException("The thread was interrupted while waiting for a response", ex);
            }
        }
        if (timedOut.get()) {
            throw new TimeoutException();
        }
        final ApplicationException error = errorContainer.get();
        if (error != null) {
            throw error;
        }
    }
}
