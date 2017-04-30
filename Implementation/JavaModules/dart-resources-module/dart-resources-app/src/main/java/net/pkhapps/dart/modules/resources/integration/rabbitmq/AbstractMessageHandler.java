package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.StatusCodes;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.MessageConverter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.MessageConvertionException;
import net.pkhapps.dart.modules.resources.RabbitMQProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.time.Clock;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * TODO document me
 */
abstract class AbstractMessageHandler<M> extends DefaultConsumer {

    final Logger logger = LoggerFactory.getLogger(getClass());
    private final Class<M> messageClass;
    private final RabbitMQProperties rabbitMQProperties;
    private final Clock clock;
    private final ExecutorService executorService;
    private final MessageConverter messageConverter;

    AbstractMessageHandler(Channel channel, Class<M> messageClass, RabbitMQProperties rabbitMQProperties,
                           Clock clock, ExecutorService executorService, MessageConverter messageConverter) {
        super(channel);
        this.messageClass = messageClass;
        this.rabbitMQProperties = rabbitMQProperties;
        this.clock = clock;
        this.executorService = executorService;
        this.messageConverter = messageConverter;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        try {
            M message = messageConverter.toPojo(messageClass, body, properties);
            logger.trace("Acknowledging message [{}]", envelope.getDeliveryTag());
            getChannel().basicAck(envelope.getDeliveryTag(), false);
            executorService.submit(() -> {
                try {
                    handleMessage(message, properties);
                } catch (IOException ex) {
                    logger.error("Could not handle message", ex);
                }
            });
        } catch (MessageConvertionException ex) {
            logger.warn("Could not convert incoming message, ignoring", ex);
        }
    }

    /**
     * @param message
     * @param properties
     * @throws IOException
     */
    protected abstract void handleMessage(@NotNull M message, @NotNull AMQP.BasicProperties properties)
            throws IOException;

    /**
     * @param properties
     * @return
     */
    private AMQP.BasicProperties.Builder buildReplyProperties(@NotNull AMQP.BasicProperties properties) {
        // @formatter:off
        return new AMQP.BasicProperties.Builder()
                .timestamp(Date.from(clock.instant()))
                .appId(rabbitMQProperties.getMessageAppId().get())
                .userId(rabbitMQProperties.getUsername().get())
                .correlationId(properties.getCorrelationId())
                .expiration(rabbitMQProperties.getMessageExpiration().get());
        // @formatter:on
    }

    /**
     * @param code
     * @param message
     * @param properties
     * @throws IOException
     */
    protected void sendError(int code, @Nullable String message, @NotNull AMQP.BasicProperties properties)
            throws IOException {
        Objects.requireNonNull(properties, "properties must not be null");
        if (properties.getReplyTo() != null) {
            logger.debug("Sending error [{}: {}] to [{}]", code, message,
                    properties.getReplyTo());
            final Map<String, Object> headers = new HashMap<>();
            headers.put(StatusCodes.STATUS_CODE_HEADER, code);
            if (message != null) {
                headers.put(StatusCodes.STATUS_MESSAGE_HEADER, message);
            }
            synchronized (this) {
                getChannel()
                        .basicPublish("", properties.getReplyTo(),
                                buildReplyProperties(properties).headers(headers).build(),
                                null);
            }
        }
    }

    /**
     * @param message
     * @param properties
     * @throws IOException
     */
    protected void sendOk(@Nullable Object message, @NotNull AMQP.BasicProperties properties) throws IOException {
        Objects.requireNonNull(properties, "properties must not be null");
        if (properties.getReplyTo() != null) {
            logger.debug("Sending OK response to [{}]", properties.getReplyTo());
            AMQP.BasicProperties.Builder replyProperties = buildReplyProperties(properties)
                    .headers(Collections.singletonMap(StatusCodes.STATUS_CODE_HEADER, StatusCodes.OK));
            try {
                byte[] body = null;
                if (message != null) {
                    body = messageConverter.fromPojo(message, replyProperties);
                }
                synchronized (this) {
                    getChannel().basicPublish("", properties.getReplyTo(), replyProperties.build(), body);
                }
            } catch (MessageConvertionException ex) {
                throw new IOException("Could not convert message", ex);
            }
        }
    }
}
