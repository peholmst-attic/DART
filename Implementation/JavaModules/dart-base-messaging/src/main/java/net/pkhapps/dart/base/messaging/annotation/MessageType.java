package net.pkhapps.dart.base.messaging.annotation;

import com.rabbitmq.client.AMQP;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation set on message classes in order to specify the {@link AMQP.BasicProperties#getType() type} message
 * property.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageType {

    /**
     * The string that the {@link AMQP.BasicProperties#getType() type} message property will be set to when serializing
     * the message.
     */
    String value();
}
