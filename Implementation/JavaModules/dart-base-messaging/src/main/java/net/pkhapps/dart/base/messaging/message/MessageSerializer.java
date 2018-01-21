package net.pkhapps.dart.base.messaging.message;

import com.rabbitmq.client.AMQP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Interface defining a message serializer that is responsible for serializing a message from a POJO to an array of
 * bytes to be sent as the payload of a RabbitMQ message.
 */
@FunctionalInterface
public interface MessageSerializer {
    /**
     * Serializes the given data into a byte array.
     *
     * @param data              the data to serialize, can be null.
     * @param propertiesBuilder a builder that can be used to set any message properties.
     * @return a byte array containing the data, can be empty but never null.
     * @throws IOException in case the data cannot be serialized.
     */
    @NotNull
    byte[] serialize(@Nullable Object data, @NotNull AMQP.BasicProperties.Builder propertiesBuilder) throws IOException;
}
