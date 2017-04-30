package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer;

import com.rabbitmq.client.AMQP;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * TODO Document me!
 */
public interface IncomingMessageConverter<T> {

    /**
     * Converts the specified message bytes to a POJO.
     *
     * @param body       the body fo the AMQP message.
     * @param properties the AMQP message properties.
     * @return the message POJO.
     * @throws IOException if the message cannot be converted.
     */
    @NotNull
    T fromBytes(@NotNull byte[] body, @NotNull AMQP.BasicProperties properties) throws IOException;
}
