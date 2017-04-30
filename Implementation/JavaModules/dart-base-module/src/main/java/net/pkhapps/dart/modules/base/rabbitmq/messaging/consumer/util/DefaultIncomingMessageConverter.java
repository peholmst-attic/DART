package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer.util;

import com.rabbitmq.client.AMQP;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer.IncomingMessageConverter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.MessageConverter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.MessageConvertionException;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Objects;

/**
 * TODO Document me!
 */
public class DefaultIncomingMessageConverter<T> implements IncomingMessageConverter<T> {

    private final MessageConverter messageConverter;
    private final Class<T> pojoClass;

    /**
     * @param messageConverter
     * @param pojoClass
     * @throws JAXBException
     */
    public DefaultIncomingMessageConverter(@NotNull MessageConverter messageConverter,
                                           @NotNull Class<T> pojoClass) throws JAXBException {
        this.messageConverter = Objects.requireNonNull(messageConverter, "messageConverter must not be null");
        this.pojoClass = Objects.requireNonNull(pojoClass, "pojoClass must not be null");
    }

    @NotNull
    @Override
    public T fromBytes(@NotNull byte[] body, @NotNull AMQP.BasicProperties properties) throws IOException {
        try {
            return messageConverter.toPojo(pojoClass, body, properties);
        } catch (MessageConvertionException ex) {
            throw new IOException("Error converting from bytes", ex);
        }
    }
}
