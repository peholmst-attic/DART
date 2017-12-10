package net.pkhapps.dart.modules.base.rabbitmq.messaging;

import com.rabbitmq.client.AMQP;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * TODO Document me!
 */
public interface MessageConverter {

    @NotNull <T> T toPojo(@NotNull byte[] body, @NotNull AMQP.BasicProperties properties, @NotNull Class<T> pojoClass)
            throws MessageConversionException;

    void fromPojo(@NotNull Object pojo, @NotNull ByteBuffer byteBuffer,
                  @NotNull AMQP.BasicProperties.Builder propertiesBuilder)
            throws MessageConversionException;
}
