package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer;

import com.rabbitmq.client.AMQP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * TODO Document me!
 */
public interface OutgoingMessageConverter<T> {

    /**
     * Converts the specified message to a byte array and populates the AMQP properties.
     *
     * @param message         the message to convert.
     * @param propertyBuilder an AMQP properties builder that can be used to specify additional properties.
     * @return a byte array to be send as the body of the message.
     * @throws IOException if the message cannot be converted.
     */
    @NotNull
    byte[] toBytes(@NotNull T message, @NotNull AMQP.BasicProperties.Builder propertyBuilder) throws IOException;

    /**
     * Gets the RabbitMQ routing key of the given message.
     *
     * @param message the message to send.
     * @return the routing key, or {@code null} to use an empty routing key.
     */
    @Nullable
    String getRoutingKey(@NotNull T message);

    /**
     * Gets the RabbitMQ exchange of the given message.
     *
     * @param message the message to send.
     * @return the exchange, or {@code null} to use the default exchange.
     */
    @Nullable
    String getExchange(@NotNull T message);
}
