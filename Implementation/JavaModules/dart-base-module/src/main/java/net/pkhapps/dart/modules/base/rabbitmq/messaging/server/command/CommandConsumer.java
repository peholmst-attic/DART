package net.pkhapps.dart.modules.base.rabbitmq.messaging.server.command;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import io.reactivex.schedulers.Schedulers;
import net.pkhapps.dart.modules.base.messaging.AccessDeniedException;
import net.pkhapps.dart.modules.base.messaging.HandlerNotFoundException;
import net.pkhapps.dart.modules.base.messaging.command.Command;
import net.pkhapps.dart.modules.base.messaging.command.CommandBroker;
import net.pkhapps.dart.modules.base.rabbitmq.RabbitMQProperties;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.ErrorCode;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.MessageConversionException;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.MessageConverter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.StatusCodes;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Clock;
import java.util.*;

/**
 * TODO Document me!
 */
public class CommandConsumer extends DefaultConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandConsumer.class);
    private final Clock clock;
    private final RabbitMQProperties rabbitMQProperties;
    private final MessageConverter messageConverter;
    private final CommandBroker commandBroker;

    public CommandConsumer(@NotNull Channel channel, @NotNull Clock clock,
                           @NotNull RabbitMQProperties rabbitMQProperties,
                           @NotNull MessageConverter messageConverter,
                           @NotNull CommandBroker commandBroker) {
        super(channel);
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
        this.rabbitMQProperties = Objects.requireNonNull(rabbitMQProperties, "rabbitMqProperties must not be null");
        this.messageConverter = Objects.requireNonNull(messageConverter, "messageConverter must not be null");
        this.commandBroker = Objects.requireNonNull(commandBroker, "commandBroker must not be null");
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        try {
            Command command = messageConverter.toPojo(body, properties, Command.class);
            LOGGER.trace("Acknowledging message [{}]", envelope.getDeliveryTag());
            synchronized (this) {
                getChannel().basicAck(envelope.getDeliveryTag(), false);
            }
            handleCommand(command, properties);
        } catch (MessageConversionException ex) {
            LOGGER.warn("Could not convert incoming message, ignoring", ex);
        }
    }

    private void handleCommand(@NotNull Command command, @NotNull AMQP.BasicProperties properties) throws IOException {
        try {
            // TODO Check that I've gotten the schedulers right
            commandBroker.handleCommand(command, properties.getUserId())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.io())
                    .subscribe(() -> sendOk(properties), error -> sendError(error, properties));
        } catch (HandlerNotFoundException ex) {
            LOGGER.warn("Found no handler for command [{}], ignoring", command);
        }
    }

    private void sendOk(@NotNull AMQP.BasicProperties properties) throws IOException {
        Objects.requireNonNull(properties, "properties must not be null");
        if (properties.getReplyTo() != null) {
            LOGGER.debug("Sending OK response to [{}]", properties.getReplyTo());
            AMQP.BasicProperties.Builder replyProperties = buildReplyProperties(properties)
                    .headers(Collections.singletonMap(StatusCodes.STATUS_CODE_HEADER, StatusCodes.OK));
            synchronized (this) {
                getChannel().basicPublish("", properties.getReplyTo(), replyProperties.build(), null);
            }
        }

    }

    private void sendError(@NotNull Throwable throwable, @NotNull AMQP.BasicProperties properties) throws IOException {
        Objects.requireNonNull(throwable, "throwable must not be null");
        Objects.requireNonNull(properties, "properties must not be null");
        if (properties.getReplyTo() != null) {
            final int code = getErrorCode(throwable);
            final String message = throwable.getMessage();
            LOGGER.debug("Sending error [{}: {}] to [{}]", code, message,
                    properties.getReplyTo());
            final Map<String, Object> headers = new HashMap<>();
            headers.put(StatusCodes.STATUS_CODE_HEADER, code);
            if (message != null) {
                headers.put(StatusCodes.STATUS_MESSAGE_HEADER, message);
            }
            synchronized (this) {
                getChannel().basicPublish("", properties.getReplyTo(),
                        buildReplyProperties(properties).headers(headers).build(), null);
            }
        }
    }

    private int getErrorCode(@NotNull Throwable throwable) {
        if (throwable.getClass().isAnnotationPresent(ErrorCode.class)) {
            return throwable.getClass().getAnnotation(ErrorCode.class).value();
        } else if (throwable instanceof AccessDeniedException) {
            return StatusCodes.FORBIDDEN;
        } else {
            return StatusCodes.SYSTEM_ERROR;
        }
    }

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

}
