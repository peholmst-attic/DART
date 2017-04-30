package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer.util;

import com.rabbitmq.client.AMQP;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer.OutgoingMessageConverter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.MessageConverter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.MessageConvertionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Objects;

/**
 * TODO Document me!
 */
public class DefaultOutgoingMessageConverter<T> implements OutgoingMessageConverter<T> {

    private final MessageConverter messageConverter;
    private final MessageRouter messageRouter;

    public DefaultOutgoingMessageConverter(@NotNull MessageConverter messageConverter,
                                           @NotNull MessageRouter messageRouter) throws JAXBException {
        this.messageConverter = Objects.requireNonNull(messageConverter, "messageConverter must not be null");
        this.messageRouter = Objects.requireNonNull(messageRouter, "messageRouter must not be null");
    }

    @NotNull
    @Override
    public byte[] toBytes(@NotNull T message, @NotNull AMQP.BasicProperties.Builder propertyBuilder)
            throws IOException {
        try {
            return messageConverter.fromPojo(message, propertyBuilder);
        } catch (MessageConvertionException ex) {
            throw new IOException("Error converting to bytes", ex);
        }
    }

    @Nullable
    @Override
    public String getRoutingKey(@NotNull T message) {
        return messageRouter.getRoutingKey(message);
    }

    @Nullable
    @Override
    public String getExchange(@NotNull T message) {
        return messageRouter.getExchange(message);
    }
}
