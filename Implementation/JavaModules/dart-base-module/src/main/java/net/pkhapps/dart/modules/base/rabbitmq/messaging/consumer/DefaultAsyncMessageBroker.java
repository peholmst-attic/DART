package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import net.pkhapps.dart.modules.base.rabbitmq.RabbitMQChannelManager;
import net.pkhapps.dart.modules.base.rabbitmq.RabbitMQProperties;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.StatusCodes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO Document me!
 */
@ApplicationScoped
public class DefaultAsyncMessageBroker extends RabbitMQChannelManager implements AsyncMessageBroker {

    private static final String REPLY_TO = "amq.rabbitmq.reply-to";

    private final Map<String, CallbackEntry> waitingCallbacks = new ConcurrentHashMap<>();
    private final AtomicLong nextCorrelationId = new AtomicLong();

    private Channel channel;

    @Inject
    public DefaultAsyncMessageBroker(ScheduledExecutorService executorService,
                                     RabbitMQProperties rabbitMQProperties) {
        super(executorService, rabbitMQProperties);
    }

    @Override
    protected void setUp(Channel channel) throws Exception {
        ResponseConsumer responseConsumer = new ResponseConsumer(channel);
        channel.basicConsume(REPLY_TO, true, responseConsumer);
        logger.info("Listening for responses on queue [{}]", REPLY_TO);
        this.channel = channel;
    }

    @Override
    protected void tearDown(Channel channel) throws Exception {
        this.channel = null;
    }

    @Override
    public <REQUEST, RESPONSE> void sendRequest(@NotNull REQUEST request,
                                                @NotNull CallbackWithResponse<RESPONSE> callback,
                                                @NotNull OutgoingMessageConverter<REQUEST>
                                                        requestConverter,
                                                @NotNull IncomingMessageConverter<RESPONSE>
                                                        responseConverter)
            throws IOException {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(callback, "callback must not be null");
        Objects.requireNonNull(requestConverter, "requestConverter must not be null");
        Objects.requireNonNull(responseConverter, "responseConverter must not be null");
        sendMessage(request, callback, requestConverter, responseConverter);
    }

    @Override
    public <COMMAND> void sendCommand(@NotNull COMMAND command, @Nullable CallbackWithoutResponse callback,
                                      @NotNull OutgoingMessageConverter<COMMAND> commandConverter)
            throws IOException {
        Objects.requireNonNull(command, "command must not be null");
        Objects.requireNonNull(commandConverter, "commandConverter must not be null");
        sendMessage(command, callback, commandConverter, null);
    }

    private <MESSAGE> void sendMessage(MESSAGE message, Callback callback,
                                       OutgoingMessageConverter<MESSAGE> messageConverter,
                                       IncomingMessageConverter<?> responseConverter)
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
            builder.replyTo(REPLY_TO);
            logger.trace(
                    "Sending message {[]} to exchange [{}] using routing key [{}] and correlation ID [{}], " +
                    "expecting reply to [{}]",
                    message, exchange, routingKey, correlationId, REPLY_TO);
        } else {
            // If there is no callback, we're "firing and forgetting". The receiving end will not
            // even try to send a reply when there is no replyTo queue.
            logger.trace(
                    "Sending message {[]} to exchange [{}] using routing key [{}] and correlation ID [{}], no " +
                    "reply destination",
                    message, exchange, routingKey, correlationId);
        }
        synchronized (this) {
            if (channel == null || !channel.isOpen()) {
                throw new IOException("No open RabbitMQ channel");
            }
            channel.basicPublish(exchange, routingKey, builder.build(), body);
        }

        if (callback != null) {
            final ScheduledFuture<?> timeoutFuture = executorService.schedule(() -> {
                logger.trace("Callback for correlation ID [{}] timed out", correlationId);
                CallbackEntry waitingCallback = waitingCallbacks.remove(correlationId);
                if (waitingCallback != null) {
                    waitingCallback.callback.onTimeout();
                }
            }, rabbitMQProperties.getRpcResponseTimeoutMs().get(), TimeUnit.MILLISECONDS);
            waitingCallbacks.put(correlationId, new CallbackEntry(callback, responseConverter, timeoutFuture));
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
            final String correlationId = properties.getCorrelationId();
            final CallbackEntry callback = waitingCallbacks.remove(correlationId);
            if (callback != null) {
                logger.trace("Received response for callback with correlation ID [{}]", correlationId);
                callback.timeoutFuture.cancel(false);
                Integer statusCode = (Integer) properties.getHeaders().get(StatusCodes.STATUS_CODE_HEADER);
                if (statusCode == null || statusCode == 200) {
                    if (callback.callback instanceof CallbackWithoutResponse) {
                        ((CallbackWithoutResponse) callback.callback).onSuccess();
                    } else if (callback.callback instanceof CallbackWithResponse && callback.messageConverter != null) {
                        Object response = callback.messageConverter.fromBytes(body, properties);
                        ((CallbackWithResponse) callback.callback).onSuccess(response);
                    }
                } else {
                    String statusMessage = (String) properties.getHeaders().get(StatusCodes.STATUS_MESSAGE_HEADER);
                    logger.trace("Response to callback with correlation ID [{}] contained an error: {}: {}",
                            correlationId, statusCode, statusMessage);
                    callback.callback.onError(statusCode, statusMessage);
                }
            } else {
                logger.trace("Received unexpected response, ignoring");
            }
        }
    }

    private static class CallbackEntry {
        final Callback callback;
        final IncomingMessageConverter<?> messageConverter;
        final ScheduledFuture<?> timeoutFuture;

        CallbackEntry(Callback callback,
                      IncomingMessageConverter<?> messageConverter,
                      ScheduledFuture<?> timeoutFuture) {
            this.callback = callback;
            this.messageConverter = messageConverter;
            this.timeoutFuture = timeoutFuture;
        }
    }
}
