package net.pkhapps.dart.statuspanel.messaging;

import android.support.annotation.NonNull;

import com.rabbitmq.client.AMQP;

import java.io.IOException;

/**
 * A converter interface for converting incoming RabbitMQ messages into POJOs.
 */
public interface IIncomingMessageConverter<T> {

    /**
     * Converts the specified message bytes to a POJO.
     *
     * @param body       the body fo the AMQP message.
     * @param properties the AMQP message properties.
     * @return the message POJO.
     * @throws IOException if the message cannot be converted.
     */
    @NonNull
    T fromBytes(@NonNull byte[] body, @NonNull AMQP.BasicProperties properties) throws IOException;
}
