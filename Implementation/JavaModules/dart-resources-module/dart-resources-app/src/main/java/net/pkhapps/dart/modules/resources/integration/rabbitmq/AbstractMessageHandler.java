package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import net.pkhapps.dart.modules.resources.RabbitMQProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.time.Clock;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO document me
 */
abstract class AbstractMessageHandler extends DefaultConsumer {

    final Logger logger = LoggerFactory.getLogger(getClass());
    private final RabbitMQProperties rabbitMQProperties;
    private final Clock clock;

    AbstractMessageHandler(Channel channel, RabbitMQProperties rabbitMQProperties, Clock clock) {
        super(channel);
        this.rabbitMQProperties = rabbitMQProperties;
        this.clock = clock;
    }

    /**
     * @param properties
     * @return
     */
    AMQP.BasicProperties.Builder buildReplyProperties(@NotNull AMQP.BasicProperties properties) {
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
    void sendError(int code, @Nullable String message, @NotNull AMQP.BasicProperties properties) throws IOException {
        logger.debug("Sending error [{}: {}] to [{}]", code, message,
                properties.getReplyTo());
        Map<String, Object> headers = new HashMap<>();
        headers.put(StatusCodes.STATUS_CODE_HEADER, code);
        if (message != null) {
            headers.put(StatusCodes.STATUS_MESSAGE_HEADER, message);
        }
        getChannel()
                .basicPublish("", properties.getReplyTo(), buildReplyProperties(properties).headers(headers).build(),
                        null);
    }
}
