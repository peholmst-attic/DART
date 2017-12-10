package net.pkhapps.dart.statuspanel.messaging;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rabbitmq.client.AMQP;

import java.io.IOException;

/**
 * A converter interface for converting outgoing message POJOs into RabbitMQ messages.
 */
public interface IOutgoingMessageConverter<T> {

    /**
     * Converts the specified message to a byte array and populates the AMQP properties.
     *
     * @param message         the message to convert.
     * @param propertyBuilder an AMQP properties builder that can be used to specify additional properties.
     * @return a byte array to be send as the body of the message.
     * @throws IOException if the message cannot be converted.
     */
    @NonNull
    byte[] toBytes(@NonNull T message, @NonNull AMQP.BasicProperties.Builder propertyBuilder) throws IOException;

    /**
     * Gets the RabbitMQ routing key of the given message.
     *
     * @param message the message to send.
     * @return the routing key, or {@code null} to use an empty routing key.
     */
    @Nullable
    String getRoutingKey(@NonNull T message);

    /**
     * Gets the RabbitMQ exchange of the given message.
     *
     * @param message the message to send.
     * @return the exchange, or {@code null} to use the default exchange.
     */
    @Nullable
    String getExchange(@NonNull T message);
}
