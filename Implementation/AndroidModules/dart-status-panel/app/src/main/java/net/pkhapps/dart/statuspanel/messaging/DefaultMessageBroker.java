package net.pkhapps.dart.statuspanel.messaging;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Default implementation of {@link IMessageBroker}.
 */
public class DefaultMessageBroker implements IMessageBroker {

    private static final String TAG = "DefaultMessageBroker";
    private static final int RESPONSE_TIMEOUT_MS = 5000;

    private ScheduledExecutorService timeoutExecutionService;
    private Channel channel;
    private String replyTo;

    private final AtomicLong nextCorrelationId = new AtomicLong();
    private final ConcurrentMap<String, CallbackEntry> activeCallbacks
            = new ConcurrentHashMap<>();

    /**
     * Sets the RabbitMQ channel that the message broker should use. The message broker will automatically
     * detect when the channel is shutdown and perform the necessary housekeeping duties.
     *
     * @param channel the RabbitMQ channel to use for communication.
     * @throws IOException if something goes wrong while setting up the message broker.
     */
    public synchronized void setChannel(@NonNull Channel channel) throws IOException {
        this.channel = channel;
        this.replyTo = this.channel.queueDeclare().getQueue();
        channel.addShutdownListener(new ShutdownListener() {
            @Override
            public void shutdownCompleted(ShutdownSignalException cause) {
                Log.i(TAG, "RabbitMQ channel shutdown, cancelling all waiting callbacks");
                synchronized (DefaultMessageBroker.this) {
                    timeoutExecutionService.shutdownNow();
                    timeoutExecutionService = null;
                    DefaultMessageBroker.this.channel = null;
                }
                for (String key : activeCallbacks.keySet()) {
                    CallbackEntry callback = activeCallbacks.remove(key);
                    if (callback != null) {
                        try {
                            callback.callback.onTimeout();
                        } catch (Exception ex) {
                            // Ignore it
                        }
                    }
                }
            }
        });

        // Create new execution service
        timeoutExecutionService = Executors.newSingleThreadScheduledExecutor();

        // Set up response consumer
        ResponseConsumer responseConsumer = new ResponseConsumer(channel);
        channel.basicConsume(this.replyTo, responseConsumer);
    }

    @NonNull
    private synchronized Channel getChannel() throws IOException {
        if (channel == null || !channel.isOpen()) {
            throw new IOException("No open RabbitMQ channel");
        }
        return channel;
    }

    @NonNull
    private synchronized String getReplyTo() throws IOException {
        if (replyTo == null) {
            throw new IOException("No replyTo queue declared");
        }
        return replyTo;
    }

    @NonNull
    private synchronized ScheduledExecutorService getTimeoutExecutionService() throws IOException {
        if (timeoutExecutionService == null || timeoutExecutionService.isShutdown()) {
            throw new IOException("No active timeout execution service");
        }
        return timeoutExecutionService;
    }

    @Override
    public <REQUEST, RESPONSE> void sendRequest(@NonNull REQUEST request,
                                                @NonNull ICallbackWithResponse<RESPONSE> callback,
                                                @NonNull IOutgoingMessageConverter<REQUEST> requestConverter,
                                                @NonNull IIncomingMessageConverter<RESPONSE> responseConverter)
            throws IOException {
        sendMessage(request, callback, requestConverter, responseConverter);
    }

    @Override
    public <COMMAND> void sendCommand(@NonNull COMMAND command,
                                      @Nullable ICallbackWithoutResponse callback,
                                      @NonNull IOutgoingMessageConverter<COMMAND> commandConverter)
            throws IOException {
        sendMessage(command, callback, commandConverter, null);
    }

    private <MESSAGE> void sendMessage(@NonNull MESSAGE message, @Nullable ICallback callback,
                                       @NonNull IOutgoingMessageConverter<MESSAGE> messageConverter,
                                       @Nullable IIncomingMessageConverter<?> responseConverter)
            throws IOException {
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        byte[] body = messageConverter.toBytes(message, builder);
        String exchange = messageConverter.getExchange(message);
        if (exchange == null) {
            exchange = "";
        }
        String routingKey = messageConverter.getRoutingKey(message);
        if (routingKey == null) {
            routingKey = "";
        }
        final String correlationId = String.valueOf(nextCorrelationId.getAndIncrement());
        builder.correlationId(correlationId);
        if (callback != null) {
            // If there is no callback, we're "firing and forgetting". The receiving end will not
            // even try to send a reply when there is no replyTo queue.
            builder.replyTo(getReplyTo());
        }
        getChannel().basicPublish(exchange, routingKey, builder.build(), body);

        if (callback != null) {
            ScheduledFuture<?> timeoutFuture = getTimeoutExecutionService().schedule(new Runnable() {
                @Override
                public void run() {
                    CallbackEntry callback = activeCallbacks.remove(correlationId);
                    if (callback != null) {
                        callback.callback.onTimeout();
                    }
                }
            }, RESPONSE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            activeCallbacks.put(correlationId, new CallbackEntry(callback, responseConverter, timeoutFuture));
        }
    }

    private class ResponseConsumer extends DefaultConsumer {

        ResponseConsumer(Channel channel) {
            super(channel);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void handleDelivery(String consumerTag, Envelope envelope,
                                   AMQP.BasicProperties properties, byte[] body) throws IOException {
            // Acknowledge immediately.
            getChannel().basicAck(envelope.getDeliveryTag(), false);
            CallbackEntry callback = activeCallbacks.remove(properties.getCorrelationId());
            if (callback != null) {
                Log.d(TAG, "Received expected response");
                callback.timeoutFuture.cancel(true);
                Integer statusCode = (Integer) properties.getHeaders().get("statusCode");
                if (statusCode == null || statusCode == 200) {
                    if (callback.callback instanceof ICallbackWithoutResponse) {
                        ((ICallbackWithoutResponse) callback.callback).onSuccess();
                    } else if (callback.callback instanceof ICallbackWithResponse && callback.messageConverter != null) {
                        Object response = callback.messageConverter.fromBytes(body, properties);
                        ((ICallbackWithResponse) callback.callback).onSuccess(response);
                    }
                } else {
                    String statusMessage = (String) properties.getHeaders().get("statusMessage");
                    callback.callback.onError(statusCode, statusMessage);
                }
            } else {
                Log.d(TAG, "Received unexpected response");
            }
        }
    }

    private static class CallbackEntry {
        final ICallback callback;
        final IIncomingMessageConverter<?> messageConverter;
        final ScheduledFuture<?> timeoutFuture;

        CallbackEntry(@NonNull ICallback callback,
                      @Nullable IIncomingMessageConverter<?> messageConverter,
                      @NonNull ScheduledFuture<?> timeoutFuture) {
            this.callback = callback;
            this.messageConverter = messageConverter;
            this.timeoutFuture = timeoutFuture;
        }
    }
}
