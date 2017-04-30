package net.pkhapps.dart.modules.base.rabbitmq.messaging.util;

import com.rabbitmq.client.AMQP;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me!
 */
public interface MessageConverter {

    <T> T toPojo(@NotNull Class<T> pojoClass, @NotNull byte[] body, @NotNull AMQP.BasicProperties properties)
            throws MessageConvertionException;

    <T> byte[] fromPojo(@NotNull T pojo, @NotNull AMQP.BasicProperties.Builder propertyBuilder)
            throws MessageConvertionException;
}
